package com.github.imdmk.doublejump;

import com.github.imdmk.doublejump.command.argument.PlayerArgument;
import com.github.imdmk.doublejump.command.context.PlayerContext;
import com.github.imdmk.doublejump.command.handler.NotificationHandler;
import com.github.imdmk.doublejump.command.handler.PermissionHandler;
import com.github.imdmk.doublejump.command.handler.UsageHandler;
import com.github.imdmk.doublejump.configuration.ConfigurationFactory;
import com.github.imdmk.doublejump.configuration.implementation.PluginConfiguration;
import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.jump.JumpPlayerService;
import com.github.imdmk.doublejump.jump.command.DoubleJumpCommand;
import com.github.imdmk.doublejump.jump.command.DoubleJumpForCommand;
import com.github.imdmk.doublejump.jump.item.JumpItemService;
import com.github.imdmk.doublejump.jump.item.command.DoubleJumpItemCommand;
import com.github.imdmk.doublejump.jump.item.listener.JumpItemActionBlockListener;
import com.github.imdmk.doublejump.jump.item.listener.JumpItemDisableListener;
import com.github.imdmk.doublejump.jump.item.listener.JumpItemDropListener;
import com.github.imdmk.doublejump.jump.item.listener.JumpItemEnableListener;
import com.github.imdmk.doublejump.jump.item.listener.JumpItemInteractListener;
import com.github.imdmk.doublejump.jump.listener.DoubleJumpListener;
import com.github.imdmk.doublejump.jump.listener.JumpDisableListener;
import com.github.imdmk.doublejump.jump.listener.JumpEnableListener;
import com.github.imdmk.doublejump.jump.listener.JumpFallDamageListener;
import com.github.imdmk.doublejump.jump.listener.JumpRefreshListener;
import com.github.imdmk.doublejump.jump.listener.JumpRegenerationListener;
import com.github.imdmk.doublejump.jump.listener.JumpStreakResetListener;
import com.github.imdmk.doublejump.jump.placeholder.JumpPlayerStreakPlaceholder;
import com.github.imdmk.doublejump.jump.placeholder.delay.JumpPlayerDelayPlaceholder;
import com.github.imdmk.doublejump.jump.placeholder.delay.JumpPlayerIsDelayPlaceholder;
import com.github.imdmk.doublejump.jump.placeholder.delay.JumpPlayerRegenerationDelayPlaceholder;
import com.github.imdmk.doublejump.jump.placeholder.jumps.JumpPlayerHasJumpsPlaceholder;
import com.github.imdmk.doublejump.jump.placeholder.jumps.JumpPlayerJumpsLimitPlaceholder;
import com.github.imdmk.doublejump.jump.placeholder.jumps.JumpPlayerJumpsPlaceholder;
import com.github.imdmk.doublejump.jump.restriction.JumpRestrictionService;
import com.github.imdmk.doublejump.jump.sound.JumpSoundService;
import com.github.imdmk.doublejump.notification.Notification;
import com.github.imdmk.doublejump.notification.NotificationSender;
import com.github.imdmk.doublejump.placeholder.PlaceholderRegistry;
import com.github.imdmk.doublejump.region.RegionProvider;
import com.github.imdmk.doublejump.region.impl.EmptyRegionProvider;
import com.github.imdmk.doublejump.region.impl.WorldGuardRegionProvider;
import com.github.imdmk.doublejump.scheduler.TaskScheduler;
import com.github.imdmk.doublejump.scheduler.TaskSchedulerImpl;
import com.github.imdmk.doublejump.update.UpdateListener;
import com.github.imdmk.doublejump.update.UpdateService;
import com.google.common.base.Stopwatch;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class DoubleJump implements DoubleJumpApi {

    private final Plugin plugin;
    private final Logger logger;
    private final Server server;

    private final PluginConfiguration pluginConfiguration;

    private final BukkitAudiences bukkitAudiences;
    private final NotificationSender notificationSender;

    private final RegionProvider regionProvider;

    private final JumpPlayerManager jumpPlayerManager;

    private final JumpPlayerService jumpPlayerService;
    private final JumpRestrictionService jumpRestrictionService;

    private LiteCommands<CommandSender> liteCommands;

    private PlaceholderRegistry placeholderRegistry;

    private final Metrics metrics;

    public DoubleJump(Plugin plugin) {
        DoubleJumpApiProvider.register(this);

        Stopwatch stopwatch = Stopwatch.createStarted();
        PluginDescriptionFile pluginDescriptionFile = plugin.getDescription();

        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.server = plugin.getServer();

        /* Configuration */
        File dataFolder = plugin.getDataFolder();

        this.pluginConfiguration = ConfigurationFactory.create(PluginConfiguration.class, new File(dataFolder, "configuration.yml"));

        /* Adventure */
        this.bukkitAudiences = BukkitAudiences.create(plugin);
        this.notificationSender = new NotificationSender(this.bukkitAudiences);

        /* Hooks */
        this.regionProvider = this.hookRegionProvider();

        /* Managers */
        this.jumpPlayerManager = new JumpPlayerManager();

        /* Services */
        this.jumpPlayerService = new JumpPlayerService(this.regionProvider, this.jumpPlayerManager, this.pluginConfiguration.jumpSettings.restrictionSettings.worldRestriction, this.pluginConfiguration.jumpSettings.restrictionSettings.gameModeRestriction, this.pluginConfiguration.doubleJumpUsePermission, this.pluginConfiguration.jumpSettings.limitSettings.enabled, this.pluginConfiguration.jumpSettings.limitSettings.limit, this.pluginConfiguration.jumpSettings.limitSettings.limitsByPermissions);

        this.jumpRestrictionService = new JumpRestrictionService(this.pluginConfiguration.jumpSettings, this.pluginConfiguration.jumpSettings.restrictionSettings, this.regionProvider, this.notificationSender);

        JumpItemService jumpItemService = new JumpItemService(this.pluginConfiguration.jumpSettings.itemSettings);
        JumpSoundService jumpSoundService = new JumpSoundService(this.pluginConfiguration);
        UpdateService updateService = new UpdateService(pluginDescriptionFile);

        /* Task Scheduler */
        TaskScheduler taskScheduler = new TaskSchedulerImpl(plugin, this.server);

        /* Listeners */
        Stream.of(
                new JumpItemActionBlockListener(this.pluginConfiguration.jumpSettings.itemSettings, jumpItemService),
                new JumpItemDisableListener(this.pluginConfiguration.jumpSettings.itemSettings, jumpItemService, this.jumpPlayerManager, this.jumpPlayerService),
                new JumpItemDropListener(this.pluginConfiguration.jumpSettings.itemSettings, jumpItemService, this.jumpPlayerService),
                new JumpItemEnableListener(this.pluginConfiguration.jumpSettings.itemSettings, this.jumpPlayerManager, this.jumpPlayerService, jumpItemService),
                new JumpItemInteractListener(this.server, this.pluginConfiguration.jumpSettings, this.pluginConfiguration.jumpSettings.itemSettings, this.notificationSender, this.jumpPlayerManager, this.jumpPlayerService, jumpItemService, this.jumpRestrictionService),
                new DoubleJumpListener(this.pluginConfiguration.jumpSettings, jumpSoundService, this.notificationSender),
                new JumpDisableListener(this.jumpPlayerManager, this.jumpPlayerService, this.jumpRestrictionService, taskScheduler),
                new JumpEnableListener(this.server, this.pluginConfiguration.jumpSettings, this.jumpPlayerManager, this.jumpPlayerService, this.jumpRestrictionService, taskScheduler),
                new JumpFallDamageListener(this.pluginConfiguration.jumpSettings, this.jumpPlayerManager),
                new JumpRefreshListener(this.jumpPlayerService, taskScheduler),
                new JumpRegenerationListener(this.pluginConfiguration.jumpSettings, this.notificationSender, this.jumpPlayerManager),
                new JumpStreakResetListener(this.server, this.pluginConfiguration.jumpSettings, this.notificationSender, this.jumpPlayerManager),
                new UpdateListener(this.logger, this.pluginConfiguration, this.notificationSender, updateService, taskScheduler)
        ).forEach(listener -> this.server.getPluginManager().registerEvents(listener, plugin));

        /* Lite Commands */
        if (this.pluginConfiguration.commandSettings.doubleJumpEnabled) {
            this.liteCommands = this.registerLiteCommands();
        }

        /* Placeholder API */
        if (this.server.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.placeholderRegistry = new PlaceholderRegistry();

            Stream.of(
                    new JumpPlayerDelayPlaceholder(pluginDescriptionFile, this.jumpPlayerManager),
                    new JumpPlayerIsDelayPlaceholder(pluginDescriptionFile, this.pluginConfiguration.placeholderSettings, this.jumpPlayerManager),
                    new JumpPlayerRegenerationDelayPlaceholder(pluginDescriptionFile, this.jumpPlayerManager),
                    new JumpPlayerHasJumpsPlaceholder(pluginDescriptionFile, this.pluginConfiguration.placeholderSettings, this.jumpPlayerManager),
                    new JumpPlayerJumpsLimitPlaceholder(pluginDescriptionFile, this.jumpPlayerManager),
                    new JumpPlayerJumpsPlaceholder(pluginDescriptionFile, this.jumpPlayerManager),
                    new JumpPlayerStreakPlaceholder(pluginDescriptionFile, this.jumpPlayerManager)
            ).forEach(this.placeholderRegistry::register);

            this.logger.info("Hooked PlaceholderAPI!");
        }

        /* Metrics */
        this.metrics = new Metrics((JavaPlugin) plugin, 19387);

        this.logger.info("Enabled plugin in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms.");
    }

    public void disable() {
        DoubleJumpApiProvider.unregister();

        if (this.liteCommands != null) {
            this.liteCommands.unregister();
        }

        if (this.placeholderRegistry != null) {
            this.placeholderRegistry.unregisterAll();
        }

        this.bukkitAudiences.close();
        this.metrics.shutdown();

        this.disableAllowFlightForOnlinePlayers();
    }

    private LiteCommands<CommandSender> registerLiteCommands() {
        return LiteCommandsBukkit.builder("DoubleJump", this.plugin, this.server)
                .settings(settings -> settings.nativePermissions(true))

                .context(Player.class, new PlayerContext())
                .argument(Player.class, new PlayerArgument(this.server, this.pluginConfiguration.notificationSettings))

                .missingPermission(new PermissionHandler(this.pluginConfiguration.notificationSettings, this.notificationSender))
                .result(Notification.class, new NotificationHandler(this.notificationSender))
                .invalidUsage(new UsageHandler(this.pluginConfiguration.notificationSettings, this.notificationSender))

                .commands(
                        new DoubleJumpCommand(this.pluginConfiguration.jumpSettings, this.notificationSender, this.jumpPlayerManager, this.jumpPlayerService, this.jumpRestrictionService),
                        new DoubleJumpForCommand(this.pluginConfiguration.jumpSettings, this.notificationSender, this.jumpPlayerService),
                        new DoubleJumpItemCommand(this.pluginConfiguration.jumpSettings.itemSettings, this.notificationSender)
                )

                .build();
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

    private RegionProvider hookRegionProvider() {
        if (this.server.getPluginManager().isPluginEnabled("WorldGuard")) {
            this.logger.info("Hooked WorldGuard!");
            return new WorldGuardRegionProvider(this.pluginConfiguration.jumpSettings.restrictionSettings.regionRestriction);
        }

        return new EmptyRegionProvider();
    }

    @NotNull
    @Override
    public RegionProvider getRegionProvider() {
        return this.regionProvider;
    }

    @Nonnull
    @Override
    public JumpPlayerManager getJumpPlayerManager() {
        return this.jumpPlayerManager;
    }

    @NotNull
    @Override
    public JumpPlayerService getJumpPlayerService() {
        return this.jumpPlayerService;
    }
}
