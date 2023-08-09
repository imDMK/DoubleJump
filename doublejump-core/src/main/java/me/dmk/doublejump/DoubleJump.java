package me.dmk.doublejump;

import com.eternalcode.gitcheck.git.GitException;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.adventure.platform.LiteBukkitAdventurePlatformFactory;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import me.dmk.doublejump.command.argument.PlayerArgument;
import me.dmk.doublejump.command.handler.MissingPermissionHandler;
import me.dmk.doublejump.command.handler.NotificationHandler;
import me.dmk.doublejump.command.handler.UsageHandler;
import me.dmk.doublejump.configuration.PluginConfiguration;
import me.dmk.doublejump.configuration.serializer.pack.DoubleJumpPack;
import me.dmk.doublejump.hook.WorldGuardHook;
import me.dmk.doublejump.jump.JumpPlayerManager;
import me.dmk.doublejump.jump.command.DoubleJumpCommand;
import me.dmk.doublejump.jump.command.DoubleJumpCommandEditor;
import me.dmk.doublejump.jump.item.command.DoubleJumpItemCommand;
import me.dmk.doublejump.jump.item.listener.JumpItemActionBlockListener;
import me.dmk.doublejump.jump.item.listener.JumpItemDisableListener;
import me.dmk.doublejump.jump.item.listener.JumpItemDropListener;
import me.dmk.doublejump.jump.item.listener.JumpItemEnableListener;
import me.dmk.doublejump.jump.item.listener.JumpItemInteractListener;
import me.dmk.doublejump.jump.listener.DoubleJumpListener;
import me.dmk.doublejump.jump.listener.JumpDisableListener;
import me.dmk.doublejump.jump.listener.JumpEnableListener;
import me.dmk.doublejump.jump.listener.JumpFallDamageListener;
import me.dmk.doublejump.jump.listener.JumpRefreshListener;
import me.dmk.doublejump.jump.listener.JumpStreakResetListener;
import me.dmk.doublejump.notification.Notification;
import me.dmk.doublejump.notification.NotificationSender;
import me.dmk.doublejump.scheduler.TaskScheduler;
import me.dmk.doublejump.scheduler.TaskSchedulerImpl;
import me.dmk.doublejump.update.UpdateService;
import me.dmk.doublejump.util.AnsiColor;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bstats.bukkit.Metrics;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class DoubleJump implements DoubleJumpApi {

    private final Server server;
    private final Logger logger;

    private final PluginConfiguration pluginConfiguration;

    private final BukkitAudiences bukkitAudiences;
    private final NotificationSender notificationSender;

    private final WorldGuardHook worldGuardHook;

    private final JumpPlayerManager jumpPlayerManager;

    private final TaskScheduler taskScheduler;

    private LiteCommands<CommandSender> liteCommands;

    private final Metrics metrics;

    public DoubleJump(Plugin plugin) {
        DoubleJumpApiProvider.register(this);

        Instant start = Instant.now();

        this.logger = plugin.getLogger();
        this.server = plugin.getServer();

        /* Configuration */
        this.pluginConfiguration = ConfigManager.create(PluginConfiguration.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesCommons(), new DoubleJumpPack());
            it.withBindFile(new File(plugin.getDataFolder(), "configuration.yml"));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });

        /* Adventure */
        this.bukkitAudiences = BukkitAudiences.create(plugin);
        this.notificationSender = new NotificationSender(this.bukkitAudiences, MiniMessage.miniMessage());

        /* Hooks */
        this.worldGuardHook = this.hookWorldGuard();

        /* Managers */
        this.jumpPlayerManager = new JumpPlayerManager(this.worldGuardHook, this.pluginConfiguration.jumpConfiguration.disabledWorlds, this.pluginConfiguration.jumpConfiguration.disabledGameModes, this.pluginConfiguration.doubleJumpUsePermission);

        /* Task Scheduler */
        this.taskScheduler = new TaskSchedulerImpl(plugin, this.server);

        /* Listeners */
        Stream.of(
                new JumpItemActionBlockListener(this.pluginConfiguration.jumpConfiguration.jumpItemConfiguration),
                new JumpItemDisableListener(this.pluginConfiguration.jumpConfiguration.jumpItemConfiguration, this.jumpPlayerManager),
                new JumpItemDropListener(this.pluginConfiguration.jumpConfiguration.jumpItemConfiguration, this.jumpPlayerManager),
                new JumpItemEnableListener(this.pluginConfiguration.jumpConfiguration.jumpItemConfiguration, this.jumpPlayerManager),
                new JumpItemInteractListener(this.server, this.pluginConfiguration.jumpConfiguration, this.pluginConfiguration.jumpConfiguration.jumpItemConfiguration, this.pluginConfiguration.messageConfiguration, this.notificationSender, this.jumpPlayerManager, this.worldGuardHook),
                new DoubleJumpListener(this.pluginConfiguration.jumpConfiguration, this.pluginConfiguration.messageConfiguration, this.notificationSender),
                new JumpDisableListener(this.pluginConfiguration.jumpConfiguration, this.pluginConfiguration.messageConfiguration, this.notificationSender, this.jumpPlayerManager),
                new JumpEnableListener(this.server, this.pluginConfiguration.jumpConfiguration, this.pluginConfiguration.messageConfiguration, this.jumpPlayerManager, this.notificationSender, this.taskScheduler, this.worldGuardHook),
                new JumpFallDamageListener(this.pluginConfiguration.jumpConfiguration, this.jumpPlayerManager),
                new JumpRefreshListener(this.jumpPlayerManager, this.taskScheduler),
                new JumpStreakResetListener(this.server, this.pluginConfiguration.jumpConfiguration, this.pluginConfiguration.messageConfiguration, this.notificationSender, this.jumpPlayerManager)
        ).forEach(listener -> this.server.getPluginManager().registerEvents(listener, plugin));

        /* Lite Commands */
        if (this.pluginConfiguration.doubleJumpCommandEnabled) {
            this.liteCommands = this.registerLiteCommands();
        }

        /* Update check */
        if (this.pluginConfiguration.checkForUpdate) {
            try {
                new UpdateService(plugin.getDescription(), this.logger).check();
            }
            catch (GitException gitException) {
                this.logger.info(AnsiColor.RED + "An error occurred while checking for update: " + gitException.getMessage() + AnsiColor.RESET);
            }
        }

        /* Metrics */
        this.metrics = new Metrics((JavaPlugin) plugin, 19387);

        Duration timeElapsed = Duration.between(start, Instant.now());
        this.logger.info("Enabled plugin in " + timeElapsed.toMillis() + "ms.");
    }

    public void disable() {
        DoubleJumpApiProvider.unregister();

        if (this.liteCommands != null) {
            this.liteCommands.getPlatform().unregisterAll();
        }

        this.disableAllowFlightForOnlinePlayers();
        this.bukkitAudiences.close();

        this.metrics.shutdown();
    }

    private LiteCommands<CommandSender> registerLiteCommands() {
        return LiteBukkitAdventurePlatformFactory.builder(this.server, "DoubleJump", false, this.bukkitAudiences, true)
                .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>("Only player can use this command."))

                .argument(Player.class, new PlayerArgument(this.server, this.pluginConfiguration.messageConfiguration))

                .permissionHandler(new MissingPermissionHandler(this.pluginConfiguration.messageConfiguration, this.notificationSender))
                .resultHandler(Notification.class, new NotificationHandler(this.notificationSender))
                .invalidUsageHandler(new UsageHandler(this.pluginConfiguration.messageConfiguration, this.notificationSender))

                .commandInstance(
                        new DoubleJumpCommand(this.pluginConfiguration.jumpConfiguration, this.pluginConfiguration.messageConfiguration, this.notificationSender, this.jumpPlayerManager, this.worldGuardHook),
                        new DoubleJumpItemCommand(this.pluginConfiguration.jumpConfiguration.jumpItemConfiguration, this.pluginConfiguration.messageConfiguration, this.notificationSender)
                )

                .commandEditor(
                        "double-jump", new DoubleJumpCommandEditor(this.pluginConfiguration)
                )

                .register();
    }

    private void disableAllowFlightForOnlinePlayers() {
        for (Player player : this.server.getOnlinePlayers()) {
            if (!this.jumpPlayerManager.isDoubleJumpMode(player)) {
                return;
            }

            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
            }
        }
    }

    private WorldGuardHook hookWorldGuard() {
        if (this.server.getPluginManager().getPlugin("WorldGuard") != null) {
            return new WorldGuardHook(this.pluginConfiguration.jumpConfiguration.disabledRegions);
        }

        return new WorldGuardHook();
    }

    @Nonnull
    @Override
    public JumpPlayerManager getJumpPlayerManager() {
        return this.jumpPlayerManager;
    }
}
