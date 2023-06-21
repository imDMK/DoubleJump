package me.dmk.doublejump;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.adventure.platform.LiteBukkitAdventurePlatformFactory;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import me.dmk.doublejump.command.DoubleJumpCommand;
import me.dmk.doublejump.command.editor.CommandPermissionEditor;
import me.dmk.doublejump.command.handler.MissingPermissionHandler;
import me.dmk.doublejump.configuration.PluginConfiguration;
import me.dmk.doublejump.listener.PlayerDeathListener;
import me.dmk.doublejump.listener.PlayerFallDamageListener;
import me.dmk.doublejump.listener.PlayerGameModeChangeListener;
import me.dmk.doublejump.listener.PlayerJoinListener;
import me.dmk.doublejump.listener.PlayerMoveListener;
import me.dmk.doublejump.listener.PlayerQuitListener;
import me.dmk.doublejump.listener.PlayerToggleFlightListener;
import me.dmk.doublejump.notification.NotificationSender;
import me.dmk.doublejump.player.JumpPlayerManager;
import me.dmk.doublejump.player.JumpPlayerMap;
import me.dmk.doublejump.task.PlayerGroundTask;
import me.dmk.doublejump.task.scheduler.TaskScheduler;
import me.dmk.doublejump.task.scheduler.TaskSchedulerImpl;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class DoubleJump implements DoubleJumpApi {

    private final PluginConfiguration pluginConfiguration;

    private final BukkitAudiences bukkitAudiences;
    private final NotificationSender notificationSender;

    private final JumpPlayerMap jumpPlayerMap;
    private final JumpPlayerManager jumpPlayerManager;

    private LiteCommands<CommandSender> liteCommands;

    public DoubleJump(Plugin plugin) {
        DoubleJumpApiProvider.register(this);

        Logger logger = plugin.getLogger();
        Server server = plugin.getServer();

        Instant start = Instant.now();

        /* Configuration */
        this.pluginConfiguration = ConfigManager.create(PluginConfiguration.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            it.withBindFile(new File(plugin.getDataFolder(), "configuration.yml"));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });

        /* Adventure */
        this.bukkitAudiences = BukkitAudiences.create(plugin);
        this.notificationSender = new NotificationSender(this.bukkitAudiences, MiniMessage.miniMessage());

        /* Maps */
        this.jumpPlayerMap = new JumpPlayerMap();

        /* Managers */
        this.jumpPlayerManager = new JumpPlayerManager(this.pluginConfiguration.disabledWorlds, this.pluginConfiguration.disabledGameModes, this.pluginConfiguration.doubleJumpUsePermission, this.jumpPlayerMap);

        /* Task Scheduler */
        TaskScheduler taskScheduler = new TaskSchedulerImpl(plugin, server);

        /* Listeners */
        Stream.of(
                new PlayerDeathListener(this.pluginConfiguration, this.notificationSender, this.jumpPlayerMap, this.jumpPlayerManager, taskScheduler),
                new PlayerFallDamageListener(this.pluginConfiguration, this.jumpPlayerManager),
                new PlayerGameModeChangeListener(this.jumpPlayerManager, taskScheduler),
                new PlayerJoinListener(this.pluginConfiguration, this.jumpPlayerManager, taskScheduler),
                new PlayerMoveListener(this.jumpPlayerMap),
                new PlayerQuitListener(this.jumpPlayerManager),
                new PlayerToggleFlightListener(this.pluginConfiguration, this.notificationSender, this.jumpPlayerMap)
        ).forEach(listener -> server.getPluginManager().registerEvents(listener, plugin));

        /* Lite Commands */
        if (this.pluginConfiguration.doubleJumpCommandEnabled) {
            this.liteCommands = this.registerLiteCommands(plugin);
        }

        /* Ground Task */
        if (this.pluginConfiguration.jumpStreakResetOnGround) {
            taskScheduler.runTimerAsync(new PlayerGroundTask(this.pluginConfiguration, this.notificationSender, this.jumpPlayerMap, server), 10L, 10L);
        }

        Duration timeElapsed = Duration.between(start, Instant.now());
        logger.info("Enabled plugin in " + timeElapsed.toMillis() + "ms.");
    }

    public void disable() {
        DoubleJumpApiProvider.unregister();

        if (this.liteCommands != null) {
            this.liteCommands.getPlatform().unregisterAll();
        }

        this.bukkitAudiences.close();
    }

    private LiteCommands<CommandSender> registerLiteCommands(Plugin plugin) {
        return LiteBukkitAdventurePlatformFactory.builder(plugin.getServer(), plugin.getName(), true, this.bukkitAudiences, true)
                .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>("Only player can use this command."))

                .permissionHandler(new MissingPermissionHandler(this.pluginConfiguration, this.notificationSender))

                .commandInstance(
                        new DoubleJumpCommand(this.pluginConfiguration, this.notificationSender, this.jumpPlayerManager)
                )

                .commandEditor(
                        DoubleJumpCommand.class, new CommandPermissionEditor(this.pluginConfiguration)
                )

                .register();
    }

    @Nonnull
    @Override
    public JumpPlayerManager getJumpPlayerManager() {
        return this.jumpPlayerManager;
    }

    @Nonnull
    @Override
    public JumpPlayerMap getJumpPlayerMap() {
        return this.jumpPlayerMap;
    }
}
