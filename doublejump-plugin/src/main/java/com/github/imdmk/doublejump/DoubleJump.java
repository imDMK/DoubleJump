package com.github.imdmk.doublejump;

import com.eternalcode.multification.notice.Notice;
import com.github.imdmk.doublejump.configuration.ConfigurationManager;
import com.github.imdmk.doublejump.configuration.PluginConfiguration;
import com.github.imdmk.doublejump.feature.command.MissingPermissionHandler;
import com.github.imdmk.doublejump.feature.command.PlayerContextual;
import com.github.imdmk.doublejump.feature.command.UsageHandler;
import com.github.imdmk.doublejump.feature.jump.JumpConfiguration;
import com.github.imdmk.doublejump.feature.jump.PlayerFlyingService;
import com.github.imdmk.doublejump.feature.jump.command.DoubleJumpCommand;
import com.github.imdmk.doublejump.feature.jump.command.DoubleJumpReloadCommand;
import com.github.imdmk.doublejump.feature.jump.controller.DoubleJumpController;
import com.github.imdmk.doublejump.feature.jump.controller.FlightStateController;
import com.github.imdmk.doublejump.feature.jump.controller.JumpPlayerSessionController;
import com.github.imdmk.doublejump.feature.jump.fall.JumpFallDamageController;
import com.github.imdmk.doublejump.feature.jump.particle.JumpParticleController;
import com.github.imdmk.doublejump.feature.jump.restriction.JumpRestrictionController;
import com.github.imdmk.doublejump.feature.jump.restriction.JumpRestrictionService;
import com.github.imdmk.doublejump.feature.jump.restriction.delay.DelayRestrictionController;
import com.github.imdmk.doublejump.feature.jump.restriction.result.RestrictionResultNotifier;
import com.github.imdmk.doublejump.feature.message.MessageConfiguration;
import com.github.imdmk.doublejump.feature.message.MessageResultHandler;
import com.github.imdmk.doublejump.feature.message.MessageService;
import com.github.imdmk.doublejump.jump.JumpPlayerCache;
import com.github.imdmk.doublejump.shared.BukkitTaskScheduler;
import com.github.imdmk.doublejump.task.TaskScheduler;
import com.google.common.base.Stopwatch;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bstats.bukkit.Metrics;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.DependencyInjection;
import org.panda_lang.utilities.inject.DependencyInjectionException;
import org.panda_lang.utilities.inject.Injector;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * The main implementation of the DoubleJump plugin.
 * <p>
 * Responsible for initializing all systems including configuration management,
 * dependency injection, services, command registration, metrics, and plugin lifecycle.
 * <p>
 * This class implements {@link DoubleJumpApi} to expose core APIs for other parts of the plugin or external plugins.
 */
class DoubleJump implements DoubleJumpApi {

    private final Plugin plugin;
    private final Server server;
    private final Logger logger;

    private final ConfigurationManager configurationManager;

    private final MessageService messageService;

    private final TaskScheduler taskScheduler;

    private final Injector injector;

    private JumpPlayerCache jumpPlayerCache;
    private PlayerFlyingService playerFlyingService;
    private JumpRestrictionService jumpRestrictionService;
    private RestrictionResultNotifier restrictionResultNotifier;

    private LiteCommands<CommandSender> liteCommands;

    private Metrics metrics;

    /**
     * Initializes the plugin, loads configurations, injects services, registers commands and listeners.
     *
     * @param plugin the Bukkit plugin instance
     */
    DoubleJump(@NotNull Plugin plugin) {
        DoubleJumpApiProvider.register(this);
        Stopwatch stopwatch = Stopwatch.createStarted();

        this.plugin = plugin;
        this.server = plugin.getServer();
        this.logger = plugin.getLogger();

        /* Configuration */
        this.configurationManager = new ConfigurationManager(this.logger, plugin.getDataFolder());

        PluginConfiguration pluginConfiguration = this.configurationManager.create(PluginConfiguration.class);
        MessageConfiguration messageConfiguration = this.configurationManager.create(MessageConfiguration.class);
        JumpConfiguration jumpConfiguration = this.configurationManager.create(JumpConfiguration.class);

        /* Services */
        this.messageService = new MessageService(this.logger, messageConfiguration, BukkitAudiences.create(plugin), MiniMessage.miniMessage());

        /* Scheduler */
        this.taskScheduler = new BukkitTaskScheduler(plugin, this.server);

        /* Injector */
        this.injector = DependencyInjection.createInjector(resources -> {
            resources.on(Plugin.class).assignInstance(plugin);
            resources.on(Logger.class).assignInstance(this.logger);
            resources.on(Server.class).assignInstance(this.server);

            /* Configuration */
            resources.on(ConfigurationManager.class).assignInstance(this.configurationManager);
            resources.on(PluginConfiguration.class).assignInstance(pluginConfiguration);
            resources.on(MessageConfiguration.class).assignInstance(messageConfiguration);
            resources.on(JumpConfiguration.class).assignInstance(jumpConfiguration);

            /* Services */
            resources.on(MessageService.class).assignInstance(this.messageService);

            /* Scheduler */
            resources.on(TaskScheduler.class).assignInstance(this.taskScheduler);

            /* Double jump, lazy */
            resources.on(JumpPlayerCache.class).assignInstance(() -> this.jumpPlayerCache);
            resources.on(PlayerFlyingService.class).assignInstance(() -> this.playerFlyingService);
            resources.on(JumpRestrictionService.class).assignInstance(() -> this.jumpRestrictionService);
            resources.on(RestrictionResultNotifier.class).assignInstance(() -> this.restrictionResultNotifier);
        });

        try {
            this.jumpPlayerCache = this.createInstance(JumpPlayerCache.class);
            this.playerFlyingService = this.createInstance(PlayerFlyingService.class);
            this.jumpRestrictionService = this.createInstance(JumpRestrictionService.class);
            this.restrictionResultNotifier = this.createInstance(RestrictionResultNotifier.class);
        }
        catch (DependencyInjectionException injectionException) {
            this.logger.log(Level.SEVERE, "An error occurred while dependency injecting", injectionException);
            this.disablePlugin();
            return;
        }

        /* Controllers */
        Stream.of(
                DoubleJumpController.class,
                FlightStateController.class,
                JumpPlayerSessionController.class,
                JumpFallDamageController.class,
                JumpRestrictionController.class,
                DelayRestrictionController.class,
                JumpParticleController.class
        ).forEach(this::createInstance);

        /* LiteCommands */
        this.liteCommands = LiteBukkitFactory.builder("DoubleJump", plugin, this.server)
                .context(Player.class, new PlayerContextual())

                .missingPermission(new MissingPermissionHandler(this.messageService))
                .invalidUsage(new UsageHandler(this.messageService))
                .result(Notice.class, new MessageResultHandler(this.messageService))

                .commands(
                        new DoubleJumpCommand(this.messageService, this.jumpPlayerCache, this.playerFlyingService, this.jumpRestrictionService, this.restrictionResultNotifier),
                        new DoubleJumpReloadCommand(this.logger, this.configurationManager, this.messageService)
                )

                .build();

        /* Metrics */
        this.metrics = new Metrics(plugin, DoubleJumpPlugin.METRICS_SERVICE_ID);

        this.logger.info("Enabled DoubleJump plugin in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms.");
        this.logger.info("Thank you for using DoubleJump! Best regards from imDMK.");
    }

    /**
     * Disables the plugin gracefully, closing open services and unregistering commands.
     */
    void disable() {
        DoubleJumpApiProvider.unregister();

        this.configurationManager.shutdown();
        this.messageService.close();
        this.taskScheduler.shutdown();
        this.liteCommands.unregister();
        this.metrics.shutdown();

        this.logger.info("Successfully disabled plugin.");
    }

    /**
     * Creates an instance of the specified class using the dependency injector.
     *
     * @param clazz the class to instantiate
     * @param <T>   the type of the class
     * @return an instance of the specified class
     */
    private <T> T createInstance(Class<T> clazz) throws DependencyInjectionException {
        return this.injector.newInstanceWithFields(clazz);
    }

    /**
     * Forces Bukkit to disable this plugin.
     */
    private void disablePlugin() {
        this.server.getPluginManager().disablePlugin(this.plugin);
    }

    /**
     * Gets the plugin's configuration manager.
     *
     * @return the configuration manager
     */
    @Override
    public @NotNull ConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    /**
     * Gets the runtime cache for players using the double jump feature.
     *
     * @return the player cache
     */
    @Override
    public JumpPlayerCache getJumpPlayerCache() {
        return this.jumpPlayerCache;
    }
}
