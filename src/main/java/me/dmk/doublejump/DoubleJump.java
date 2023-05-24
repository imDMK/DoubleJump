package me.dmk.doublejump;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.adventure.platform.LiteBukkitAdventurePlatformFactory;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import me.dmk.doublejump.api.DoubleJumpApi;
import me.dmk.doublejump.api.DoubleJumpApiImpl;
import me.dmk.doublejump.command.DoubleJumpCommand;
import me.dmk.doublejump.command.handler.MissingPermissionHandler;
import me.dmk.doublejump.configuration.PluginConfiguration;
import me.dmk.doublejump.listener.PlayerDeathListener;
import me.dmk.doublejump.listener.PlayerGameModeChangeListener;
import me.dmk.doublejump.listener.PlayerJoinListener;
import me.dmk.doublejump.listener.PlayerQuitListener;
import me.dmk.doublejump.listener.PlayerToggleFlightListener;
import me.dmk.doublejump.notification.NotificationSender;
import me.dmk.doublejump.player.JumpPlayerManager;
import me.dmk.doublejump.player.JumpPlayerMap;
import me.dmk.doublejump.task.PlayerGroundTask;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

public class DoubleJump extends JavaPlugin {

    private PluginConfiguration pluginConfiguration;

    private BukkitAudiences bukkitAudiences;
    private NotificationSender notificationSender;

    private JumpPlayerMap jumpPlayerMap;
    private JumpPlayerManager jumpPlayerManager;

    private LiteCommands<CommandSender> liteCommands;

    private static DoubleJumpApi doubleJumpApi;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();

        /* Configuration */
        this.pluginConfiguration = ConfigManager.create(PluginConfiguration.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            it.withBindFile(new File(this.getDataFolder(), "configuration.yml"));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });

        /* Adventure */
        this.bukkitAudiences = BukkitAudiences.create(this);
        this.notificationSender = new NotificationSender(this.bukkitAudiences, MiniMessage.miniMessage());

        /* Maps */
        this.jumpPlayerMap = new JumpPlayerMap();

        /* Managers */
        this.jumpPlayerManager = new JumpPlayerManager(this, this.pluginConfiguration, this.jumpPlayerMap);

        /* Api */
        doubleJumpApi = new DoubleJumpApiImpl(this.jumpPlayerManager);

        /* Listeners */
        Stream.of(
                new PlayerDeathListener(this, this.pluginConfiguration, this.notificationSender, this.jumpPlayerMap, this.jumpPlayerManager),
                new PlayerGameModeChangeListener(this, this.jumpPlayerManager),
                new PlayerJoinListener(this.jumpPlayerManager),
                new PlayerQuitListener(this.jumpPlayerManager),
                new PlayerToggleFlightListener(this.pluginConfiguration, this.notificationSender, this.jumpPlayerMap)
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));

        /* Commands */
        if (this.pluginConfiguration.isDoubleJumpCommandEnabled()) {
            this.liteCommands = this.registerCommands();
        }

        /* Tasks */
        if (this.pluginConfiguration.isJumpStreakResetOnGround()) {
            Bukkit.getScheduler().runTaskTimer(
                    this,
                    new PlayerGroundTask(this.pluginConfiguration, this.notificationSender, this.jumpPlayerMap),
                    10L,
                    10L
            );
        }

        //Needed for server reload
        Bukkit.getScheduler().runTask(
                this,
                () -> Bukkit.getOnlinePlayers().forEach(player ->
                        this.jumpPlayerManager.enableOnJoin(player, false)
                )
        );

        long timeElapsed = System.currentTimeMillis() - start;
        this.getLogger().info("Enabled plugin in " + timeElapsed + "ms.");
    }

    @Override
    public void onDisable() {
        if (this.liteCommands != null) {
            this.liteCommands.getCommandService().getPlatform().unregisterAll();
        }

        Bukkit.getScheduler().cancelTasks(this);

        this.getLogger().info("Goodbye!");
    }

    private LiteCommands<CommandSender> registerCommands() {
        return LiteBukkitAdventurePlatformFactory.builder(this.getServer(), this.getName(), true, this.bukkitAudiences, true)
                .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>("Only player can use this command."))

                .permissionHandler(new MissingPermissionHandler(this.pluginConfiguration, this.notificationSender))

                .commandInstance(
                        new DoubleJumpCommand(this.pluginConfiguration, this.notificationSender, this.jumpPlayerManager)
                )

                .commandEditor(
                        DoubleJumpCommand.class, command -> command.permission(
                                List.of(
                                        this.pluginConfiguration.getDoubleJumpCommandPermission(),
                                        this.pluginConfiguration.getDoubleJumpUsePermission()
                                )
                        )
                )

                .register();
    }

    public static DoubleJumpApi getApi() {
        return doubleJumpApi;
    }
}
