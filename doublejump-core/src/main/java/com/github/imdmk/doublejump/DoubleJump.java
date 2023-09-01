package com.github.imdmk.doublejump;

import com.github.imdmk.doublejump.command.argument.PlayerArgument;
import com.github.imdmk.doublejump.command.handler.MissingPermissionHandler;
import com.github.imdmk.doublejump.command.handler.NotificationHandler;
import com.github.imdmk.doublejump.command.handler.UsageHandler;
import com.github.imdmk.doublejump.configuration.PluginConfiguration;
import com.github.imdmk.doublejump.configuration.serializer.ColorSerializer;
import com.github.imdmk.doublejump.configuration.serializer.ComponentSerializer;
import com.github.imdmk.doublejump.configuration.serializer.EnchantmentSerializer;
import com.github.imdmk.doublejump.configuration.serializer.ItemMetaSerializer;
import com.github.imdmk.doublejump.configuration.serializer.ItemStackSerializer;
import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.jump.command.DoubleJumpCommand;
import com.github.imdmk.doublejump.jump.command.DoubleJumpCommandEditor;
import com.github.imdmk.doublejump.jump.item.JumpItemService;
import com.github.imdmk.doublejump.jump.item.command.DoubleJumpItemCommand;
import com.github.imdmk.doublejump.jump.item.command.DoubleJumpItemCommandEditor;
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
import com.github.imdmk.doublejump.jump.particle.JumpParticleSerializer;
import com.github.imdmk.doublejump.jump.sound.JumpSoundSerializer;
import com.github.imdmk.doublejump.notification.Notification;
import com.github.imdmk.doublejump.notification.NotificationSender;
import com.github.imdmk.doublejump.notification.NotificationSerializer;
import com.github.imdmk.doublejump.region.RegionProvider;
import com.github.imdmk.doublejump.region.impl.EmptyRegionProvider;
import com.github.imdmk.doublejump.region.impl.WorldGuardRegionProvider;
import com.github.imdmk.doublejump.scheduler.TaskScheduler;
import com.github.imdmk.doublejump.scheduler.TaskSchedulerImpl;
import com.github.imdmk.doublejump.update.UpdateService;
import com.github.imdmk.doublejump.util.DurationUtil;
import com.google.common.base.Stopwatch;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.adventure.platform.LiteBukkitAdventurePlatformFactory;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
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
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class DoubleJump implements DoubleJumpApi {

    private final Server server;

    private final PluginConfiguration pluginConfiguration;

    private final BukkitAudiences bukkitAudiences;
    private final NotificationSender notificationSender;

    private final RegionProvider regionProvider;

    private final JumpItemService jumpItemService;

    private final JumpPlayerManager jumpPlayerManager;

    private final TaskScheduler taskScheduler;

    private LiteCommands<CommandSender> liteCommands;

    private final Metrics metrics;

    public DoubleJump(Plugin plugin) {
        DoubleJumpApiProvider.register(this);

        Stopwatch stopwatch = Stopwatch.createStarted();
        Logger logger = plugin.getLogger();

        this.server = plugin.getServer();

        /* Configuration */
        this.pluginConfiguration = this.createConfiguration(plugin.getDataFolder());

        /* Adventure */
        this.bukkitAudiences = BukkitAudiences.create(plugin);
        this.notificationSender = new NotificationSender(this.bukkitAudiences, MiniMessage.miniMessage());

        /* Hooks */
        this.regionProvider = this.hookRegionProvider();

        /* Services */
        this.jumpItemService = new JumpItemService(this.pluginConfiguration.jumpConfiguration.itemConfiguration);

        /* Managers */
        this.jumpPlayerManager = new JumpPlayerManager(this.regionProvider, this.pluginConfiguration.jumpConfiguration.restrictionsConfiguration.disabledWorlds, this.pluginConfiguration.jumpConfiguration.restrictionsConfiguration.disabledGameModes, this.pluginConfiguration.doubleJumpUsePermission, this.pluginConfiguration.jumpConfiguration.limitConfiguration.enabled, this.pluginConfiguration.jumpConfiguration.limitConfiguration.limit, this.pluginConfiguration.jumpConfiguration.limitConfiguration.limitsByPermissions);

        /* Task Scheduler */
        this.taskScheduler = new TaskSchedulerImpl(plugin, this.server);

        /* Listeners */
        Stream.of(
                new JumpItemActionBlockListener(this.pluginConfiguration.jumpConfiguration.itemConfiguration, this.jumpItemService),
                new JumpItemDisableListener(this.pluginConfiguration.jumpConfiguration.itemConfiguration, this.jumpItemService, this.jumpPlayerManager),
                new JumpItemDropListener(this.pluginConfiguration.jumpConfiguration.itemConfiguration, this.jumpItemService, this.jumpPlayerManager),
                new JumpItemEnableListener(this.pluginConfiguration.jumpConfiguration.itemConfiguration, this.jumpItemService, this.jumpPlayerManager),
                new JumpItemInteractListener(this.server, this.pluginConfiguration.jumpConfiguration, this.pluginConfiguration.jumpConfiguration.itemConfiguration, this.pluginConfiguration.messageConfiguration, this.notificationSender, this.jumpItemService, this.jumpPlayerManager, this.regionProvider),
                new DoubleJumpListener(this.pluginConfiguration.jumpConfiguration, this.pluginConfiguration.messageConfiguration, this.notificationSender),
                new JumpDisableListener(this.pluginConfiguration.jumpConfiguration, this.pluginConfiguration.messageConfiguration, this.notificationSender, this.jumpPlayerManager),
                new JumpEnableListener(plugin, this.server, this.pluginConfiguration.jumpConfiguration, this.pluginConfiguration.messageConfiguration, this.jumpPlayerManager, this.notificationSender, this.taskScheduler, this.regionProvider),
                new JumpFallDamageListener(this.pluginConfiguration.jumpConfiguration, this.jumpPlayerManager),
                new JumpRefreshListener(this.jumpPlayerManager, this.taskScheduler),
                new JumpRegenerationListener(this.pluginConfiguration.jumpConfiguration, this.pluginConfiguration.messageConfiguration, this.notificationSender, this.jumpPlayerManager),
                new JumpStreakResetListener(this.server, this.pluginConfiguration.jumpConfiguration, this.pluginConfiguration.messageConfiguration, this.notificationSender, this.jumpPlayerManager)
        ).forEach(listener -> this.server.getPluginManager().registerEvents(listener, plugin));

        /* Lite Commands */
        if (this.pluginConfiguration.doubleJumpCommandEnabled) {
            this.liteCommands = this.registerLiteCommands();
        }

        /* Update check */
        if (this.pluginConfiguration.checkForUpdate) {
            UpdateService updateService = new UpdateService(plugin.getDescription(), logger);
            this.taskScheduler.runLaterAsync(updateService::check, DurationUtil.toTicks(Duration.ofSeconds(3)));
        }

        /* Metrics */
        this.metrics = new Metrics((JavaPlugin) plugin, 19387);

        logger.info("Enabled plugin in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms.");
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

    private PluginConfiguration createConfiguration(File dataFolder) {
        return ConfigManager.create(PluginConfiguration.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesCommons());
            it.withSerdesPack(registry -> {
                registry.register(new ColorSerializer());
                registry.register(new ComponentSerializer());
                registry.register(new EnchantmentSerializer());
                registry.register(new ItemStackSerializer());
                registry.register(new ItemMetaSerializer());
                registry.register(new NotificationSerializer());
                registry.register(new JumpParticleSerializer());
                registry.register(new JumpSoundSerializer());
            });
            it.withBindFile(new File(dataFolder, "configuration.yml"));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });
    }

    private LiteCommands<CommandSender> registerLiteCommands() {
        return LiteBukkitAdventurePlatformFactory.builder(this.server, "DoubleJump", false, this.bukkitAudiences, true)
                .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>("Only player can use this command."))

                .argument(Player.class, new PlayerArgument(this.server, this.pluginConfiguration.messageConfiguration))

                .permissionHandler(new MissingPermissionHandler(this.pluginConfiguration.messageConfiguration, this.notificationSender))
                .resultHandler(Notification.class, new NotificationHandler(this.notificationSender))
                .invalidUsageHandler(new UsageHandler(this.pluginConfiguration.messageConfiguration, this.notificationSender))

                .commandInstance(
                        new DoubleJumpCommand(this.pluginConfiguration.jumpConfiguration, this.pluginConfiguration.messageConfiguration, this.notificationSender, this.jumpPlayerManager, this.regionProvider),
                        new DoubleJumpItemCommand(this.pluginConfiguration.jumpConfiguration.itemConfiguration, this.pluginConfiguration.messageConfiguration, this.notificationSender)
                )

                .commandEditor(DoubleJumpCommand.class, new DoubleJumpCommandEditor(this.pluginConfiguration))
                .commandEditor(DoubleJumpItemCommand.class, new DoubleJumpItemCommandEditor(this.pluginConfiguration))

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

    private RegionProvider hookRegionProvider() {
        if (this.server.getPluginManager().isPluginEnabled("WorldGuard")) {
            return new WorldGuardRegionProvider(this.pluginConfiguration.jumpConfiguration.restrictionsConfiguration.disabledRegions);
        }

        return new EmptyRegionProvider();
    }

    @Nonnull
    @Override
    public JumpPlayerManager getJumpPlayerManager() {
        return this.jumpPlayerManager;
    }
}
