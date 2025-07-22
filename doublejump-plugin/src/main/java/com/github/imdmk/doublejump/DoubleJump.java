package com.github.imdmk.doublejump;

import com.eternalcode.multification.notice.Notice;
import com.github.imdmk.doublejump.command.MissingPermissionHandler;
import com.github.imdmk.doublejump.command.PlayerContextual;
import com.github.imdmk.doublejump.command.UsageHandler;
import com.github.imdmk.doublejump.command.configurator.CommandConfig;
import com.github.imdmk.doublejump.command.configurator.CommandEditor;
import com.github.imdmk.doublejump.config.ConfigManager;
import com.github.imdmk.doublejump.config.PluginConfig;
import com.github.imdmk.doublejump.database.DatabaseConfig;
import com.github.imdmk.doublejump.database.DatabaseService;
import com.github.imdmk.doublejump.event.EventCaller;
import com.github.imdmk.doublejump.infrastructure.gui.GuiManager;
import com.github.imdmk.doublejump.infrastructure.gui.configuration.GuiConfig;
import com.github.imdmk.doublejump.infrastructure.message.MessageConfig;
import com.github.imdmk.doublejump.infrastructure.message.MessageResultHandler;
import com.github.imdmk.doublejump.infrastructure.message.MessageService;
import com.github.imdmk.doublejump.infrastructure.placeholder.PlaceholderHook;
import com.github.imdmk.doublejump.infrastructure.placeholder.PlaceholderRegistry;
import com.github.imdmk.doublejump.infrastructure.scheduler.BukkitTaskScheduler;
import com.github.imdmk.doublejump.jump.JumpConfig;
import com.github.imdmk.doublejump.jump.PlayerFlyingService;
import com.github.imdmk.doublejump.jump.cache.JumpPlayerCache;
import com.github.imdmk.doublejump.jump.controller.DoubleJumpController;
import com.github.imdmk.doublejump.jump.controller.FlightStateController;
import com.github.imdmk.doublejump.jump.controller.JumpPlayerSessionController;
import com.github.imdmk.doublejump.jump.feature.block.JumpBlockController;
import com.github.imdmk.doublejump.jump.feature.command.JumpItemCommand;
import com.github.imdmk.doublejump.jump.feature.command.JumpReloadCommand;
import com.github.imdmk.doublejump.jump.feature.command.JumpTargetCommand;
import com.github.imdmk.doublejump.jump.feature.command.JumpToggleCommand;
import com.github.imdmk.doublejump.jump.feature.command.JumpVisualCommand;
import com.github.imdmk.doublejump.jump.feature.fall.JumpFallDamageController;
import com.github.imdmk.doublejump.jump.feature.item.JumpItemService;
import com.github.imdmk.doublejump.jump.feature.item.controller.JumpItemDisableController;
import com.github.imdmk.doublejump.jump.feature.item.controller.JumpItemInteractController;
import com.github.imdmk.doublejump.jump.feature.item.controller.JumpItemResetController;
import com.github.imdmk.doublejump.jump.feature.item.controller.JumpItemRestrictionController;
import com.github.imdmk.doublejump.jump.feature.item.usage.ItemUsageStrategy;
import com.github.imdmk.doublejump.jump.feature.item.usage.ItemUsageStrategyFactory;
import com.github.imdmk.doublejump.jump.feature.placeholder.JumpAllowedPlaceholder;
import com.github.imdmk.doublejump.jump.feature.placeholder.JumpCooldownPlaceholder;
import com.github.imdmk.doublejump.jump.feature.restriction.JumpRestrictionController;
import com.github.imdmk.doublejump.jump.feature.restriction.JumpRestrictionNotifier;
import com.github.imdmk.doublejump.jump.feature.restriction.JumpRestrictionService;
import com.github.imdmk.doublejump.jump.feature.restriction.RestrictionChecker;
import com.github.imdmk.doublejump.jump.feature.restriction.cooldown.CooldownRestrictionController;
import com.github.imdmk.doublejump.jump.feature.restriction.cooldown.CooldownRestrictionService;
import com.github.imdmk.doublejump.jump.feature.velocity.JumpVelocityService;
import com.github.imdmk.doublejump.jump.feature.visual.JumpVisualService;
import com.github.imdmk.doublejump.jump.feature.visual.JumpVisualSessionController;
import com.github.imdmk.doublejump.jump.feature.visual.configuration.JumpVisualConfig;
import com.github.imdmk.doublejump.jump.feature.visual.gui.JumpVisualGui;
import com.github.imdmk.doublejump.jump.feature.visual.particle.JumpParticleController;
import com.github.imdmk.doublejump.jump.feature.visual.particle.gui.JumpParticleGui;
import com.github.imdmk.doublejump.jump.feature.visual.repository.JumpVisualCache;
import com.github.imdmk.doublejump.jump.feature.visual.repository.JumpVisualRepository;
import com.github.imdmk.doublejump.jump.feature.visual.repository.impl.DaoJumpVisualRepository;
import com.github.imdmk.doublejump.jump.feature.visual.repository.impl.EmptyJumpVisualRepository;
import com.github.imdmk.doublejump.jump.feature.visual.sound.JumpSoundController;
import com.github.imdmk.doublejump.jump.feature.visual.sound.gui.JumpSoundGui;
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

import java.sql.SQLException;
import java.util.Objects;
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

    private final ConfigManager configManager;

    private final MessageService messageService;
    private final DatabaseService databaseService;

    private final TaskScheduler taskScheduler;
    private final EventCaller eventCaller;
    private final Injector injector;

    private JumpPlayerCache jumpPlayerCache;
    private JumpVelocityService jumpVelocityService;
    private PlayerFlyingService playerFlyingService;

    private JumpVisualCache jumpVisualCache;
    private JumpVisualRepository jumpVisualRepository;
    private JumpVisualService jumpVisualService;

    private JumpRestrictionService jumpRestrictionService;
    private JumpRestrictionNotifier jumpRestrictionNotifier;

    private CooldownRestrictionService cooldownRestrictionService;

    private JumpItemService jumpItemService;
    private ItemUsageStrategy itemUsageStrategy;

    private LiteCommands<CommandSender> liteCommands;

    private PlaceholderRegistry placeholderRegistry;

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
        this.configManager = new ConfigManager(this.logger, plugin.getDataFolder());

        PluginConfig pluginConfig = this.configManager.create(PluginConfig.class);
        DatabaseConfig databaseConfig = this.configManager.create(DatabaseConfig.class);
        MessageConfig messageConfig = this.configManager.create(MessageConfig.class);
        CommandConfig commandConfig = this.configManager.create(CommandConfig.class);

        JumpConfig jumpConfig = this.configManager.create(JumpConfig.class);
        JumpVisualConfig jumpVisualConfig = this.configManager.create(JumpVisualConfig.class);

        GuiConfig guiConfig = this.configManager.create(GuiConfig.class);

        /* Services */
        this.messageService = new MessageService(this.logger, messageConfig, BukkitAudiences.create(plugin), MiniMessage.miniMessage());
        this.databaseService = new DatabaseService(this.logger, plugin.getDataFolder(), databaseConfig);

        /* Database connection, jump visual */
        this.jumpVisualCache = new JumpVisualCache();

        try {
            this.databaseService.connect();
            this.jumpVisualRepository = new DaoJumpVisualRepository(this.logger, this.databaseService.getConnectionSource(), this.jumpVisualCache);
        }
        catch (SQLException sqlException) {
            this.jumpVisualRepository = new EmptyJumpVisualRepository();

            this.logger.log(Level.SEVERE, "An error occurred while initializing the JumpVisual repository. The plugin will run, but the functions will not work as expected.", sqlException);
        }

        /* Scheduler */
        this.taskScheduler = new BukkitTaskScheduler(plugin, this.server);

        /* Event caller */
        this.eventCaller = event -> this.server.getPluginManager().callEvent(event);

        /* Gui manager */
        GuiManager guiManager = new GuiManager(this.taskScheduler);

        /* Injector */
        this.injector = DependencyInjection.createInjector(resources -> {
            resources.on(Plugin.class).assignInstance(plugin);
            resources.on(Server.class).assignInstance(this.server);
            resources.on(Logger.class).assignInstance(this.logger);

            /* Configuration */
            resources.on(ConfigManager.class).assignInstance(this.configManager);
            resources.on(PluginConfig.class).assignInstance(pluginConfig);
            resources.on(MessageConfig.class).assignInstance(messageConfig);
            resources.on(JumpConfig.class).assignInstance(jumpConfig);
            resources.on(JumpVisualConfig.class).assignInstance(jumpVisualConfig);
            resources.on(GuiConfig.class).assignInstance(guiConfig);

            /* Services */
            resources.on(MessageService.class).assignInstance(this.messageService);

            /* Scheduler */
            resources.on(TaskScheduler.class).assignInstance(this.taskScheduler);

            /* Event caller */
            resources.on(EventCaller.class).assignInstance(this.eventCaller);

            /* GuiManager */
            resources.on(GuiManager.class).assignInstance(guiManager);

            /* Jump, lazy */
            resources.on(JumpPlayerCache.class).assignInstance(() -> this.jumpPlayerCache);
            resources.on(JumpVelocityService.class).assignInstance(() -> this.jumpVelocityService);
            resources.on(PlayerFlyingService.class).assignInstance(() -> this.playerFlyingService);

            /* JumpVisual */
            resources.on(JumpVisualCache.class).assignInstance(this.jumpVisualCache);
            resources.on(JumpVisualRepository.class).assignInstance(this.jumpVisualRepository);
            resources.on(JumpVisualService.class).assignInstance(() -> this.jumpVisualService); // Lazy

            /* Jump restrictions, lazy */
            resources.on(JumpRestrictionService.class).assignInstance(() -> this.jumpRestrictionService);
            resources.on(JumpRestrictionNotifier.class).assignInstance(() -> this.jumpRestrictionNotifier);
            
            resources.on(CooldownRestrictionService.class).assignInstance(() -> this.cooldownRestrictionService);

            /* Jump items, lazy */
            resources.on(JumpItemService.class).assignInstance(() -> this.jumpItemService);
            resources.on(ItemUsageStrategy.class).assignInstance(() -> this.itemUsageStrategy);
        });

        try {
            this.jumpPlayerCache = this.createInstance(JumpPlayerCache.class);
            this.jumpVelocityService = this.createInstance(JumpVelocityService.class);
            this.playerFlyingService = this.createInstance(PlayerFlyingService.class);

            this.jumpVisualService = this.createInstance(JumpVisualService.class);

            this.jumpRestrictionService = this.createInstance(JumpRestrictionService.class);
            this.jumpRestrictionNotifier = this.createInstance(JumpRestrictionNotifier.class);

            this.cooldownRestrictionService = this.createInstance(CooldownRestrictionService.class);

            this.jumpItemService = this.createInstance(JumpItemService.class);
            this.itemUsageStrategy = ItemUsageStrategyFactory.create(jumpConfig.item.usageMode, this.injector);
        }
        catch (DependencyInjectionException injectionException) {
            this.logger.log(Level.SEVERE, "An error occurred while dependency injecting", injectionException);
            this.disablePlugin();
            return;
        }

        /* Controllers */
        Stream.of(
                /* General */
                DoubleJumpController.class,
                FlightStateController.class,
                JumpPlayerSessionController.class,

                /* Fall damage */
                JumpFallDamageController.class,

                /* Restrictions */
                JumpRestrictionController.class,
                CooldownRestrictionController.class,

                /* Jump particles */
                JumpParticleController.class,

                /* Jump sounds */
                JumpSoundController.class,

                /* Jump item */
                JumpItemDisableController.class,
                JumpItemInteractController.class,
                JumpItemResetController.class,
                JumpItemRestrictionController.class,

                /* Jump blocks */
                JumpBlockController.class,

                /* Jump visuals */
                JumpVisualSessionController.class
        ).forEach(this::createInstance);

        /* LiteCommands */
        this.liteCommands = LiteBukkitFactory.builder("DoubleJump", plugin, this.server)
                .context(Player.class, new PlayerContextual())

                .missingPermission(new MissingPermissionHandler(this.messageService))
                .invalidUsage(new UsageHandler(this.messageService))
                .result(Notice.class, new MessageResultHandler(this.messageService))

                .commands(
                        this.createInstance(JumpToggleCommand.class),
                        this.createInstance(JumpTargetCommand.class),
                        this.createInstance(JumpItemCommand.class),
                        this.createInstance(JumpVisualCommand.class),
                        this.createInstance(JumpReloadCommand.class)
                )

                .editorGlobal(new CommandEditor(this.logger, commandConfig))

                .build();

        /* Guis */
        Stream.of(
                JumpVisualGui.class,
                JumpParticleGui.class,
                JumpSoundGui.class
        ).forEach(gui -> guiManager.registerGui(this.createInstance(gui)));

        /* Placeholder API */
        PlaceholderHook placeholderHook = new PlaceholderHook(this.server, jumpConfig.placeholders);
        this.placeholderRegistry = new PlaceholderRegistry(placeholderHook);

        if (placeholderHook.isAvailable()) {
            Stream.of(
                    JumpAllowedPlaceholder.class,
                    JumpCooldownPlaceholder.class
            ).forEach(placeholder -> this.placeholderRegistry.register(this.createInstance(placeholder)));
        }

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

        this.configManager.shutdown();
        this.databaseService.close();
        this.messageService.close();
        this.taskScheduler.shutdown();
        this.liteCommands.unregister();
        this.placeholderRegistry.unregisterAll();
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

    @Override
    public @NotNull ConfigManager getConfigurationManager() {
        return Objects.requireNonNull(this.configManager, "configurationManager cannot be null");
    }

    @Override
    public @NotNull JumpPlayerCache getJumpPlayerCache() {
        return Objects.requireNonNull(this.jumpPlayerCache, "jumpPlayerCache cannot be null");
    }

    @Override
    public @NotNull RestrictionChecker getRestrictionChecker() {
        return Objects.requireNonNull(this.jumpRestrictionService, "jumpRestrictionService cannot be null");
    }

    @Override
    public @NotNull JumpVisualCache getJumpVisualCache() {
        return Objects.requireNonNull(this.jumpVisualCache, "jumpVisualCache cannot be null");
    }

    @Override
    public @NotNull JumpVisualRepository getJumpVisualRepository() {
        return Objects.requireNonNull(this.jumpVisualRepository, "jumpVisualRepository cannot be null");
    }
}
