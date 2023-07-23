package me.dmk.doublejump;

import com.eternalcode.gitcheck.GitCheck;
import com.eternalcode.gitcheck.GitCheckResult;
import com.eternalcode.gitcheck.git.GitException;
import com.eternalcode.gitcheck.git.GitRelease;
import com.eternalcode.gitcheck.git.GitRepository;
import com.eternalcode.gitcheck.git.GitTag;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.adventure.platform.LiteBukkitAdventurePlatformFactory;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import me.dmk.doublejump.command.DoubleJumpCommand;
import me.dmk.doublejump.command.argument.PlayerArgument;
import me.dmk.doublejump.command.editor.CommandPermissionEditor;
import me.dmk.doublejump.command.handler.MissingPermissionHandler;
import me.dmk.doublejump.command.handler.NotificationHandler;
import me.dmk.doublejump.command.handler.UsageHandler;
import me.dmk.doublejump.configuration.PluginConfiguration;
import me.dmk.doublejump.configuration.pack.JumpSerdesPack;
import me.dmk.doublejump.listener.DoubleJumpListener;
import me.dmk.doublejump.listener.JumpDisableListener;
import me.dmk.doublejump.listener.JumpEnableListener;
import me.dmk.doublejump.listener.JumpFallDamageListener;
import me.dmk.doublejump.listener.JumpRefreshListener;
import me.dmk.doublejump.listener.JumpStreakResetListener;
import me.dmk.doublejump.listener.item.JumpItemDisableListener;
import me.dmk.doublejump.listener.item.JumpItemDropListener;
import me.dmk.doublejump.listener.item.JumpItemEnableListener;
import me.dmk.doublejump.listener.item.JumpItemInteractListener;
import me.dmk.doublejump.notification.Notification;
import me.dmk.doublejump.notification.NotificationSender;
import me.dmk.doublejump.player.JumpPlayerManager;
import me.dmk.doublejump.task.scheduler.TaskScheduler;
import me.dmk.doublejump.task.scheduler.TaskSchedulerImpl;
import me.dmk.doublejump.util.AnsiColor;
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

    private static final GitRepository GIT_REPOSITORY = GitRepository.of("imDMK", "DoubleJump");

    private final Server server;
    private final PluginConfiguration pluginConfiguration;

    private final BukkitAudiences bukkitAudiences;
    private final NotificationSender notificationSender;

    private final JumpPlayerManager jumpPlayerManager;

    private final TaskScheduler taskScheduler;

    private LiteCommands<CommandSender> liteCommands;

    public DoubleJump(Plugin plugin) {
        DoubleJumpApiProvider.register(this);

        Logger logger = plugin.getLogger();

        Instant start = Instant.now();
        MiniMessage miniMessage = MiniMessage.miniMessage();

        this.server = plugin.getServer();

        /* Configuration */
        OkaeriSerdesPack jumpPack = new JumpSerdesPack(miniMessage);

        this.pluginConfiguration = ConfigManager.create(PluginConfiguration.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesCommons(), jumpPack);
            it.withBindFile(new File(plugin.getDataFolder(), "configuration.yml"));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });

        /* Adventure */
        this.bukkitAudiences = BukkitAudiences.create(plugin);
        this.notificationSender = new NotificationSender(this.bukkitAudiences, miniMessage);

        /* Managers */
        this.jumpPlayerManager = new JumpPlayerManager(this.pluginConfiguration.jumpConfiguration.disabledWorlds, this.pluginConfiguration.jumpConfiguration.disabledGameModes, this.pluginConfiguration.doubleJumpUsePermission);

        /* Task Scheduler */
        this.taskScheduler = new TaskSchedulerImpl(plugin, this.server);

        /* Listeners */
        Stream.of(
                new JumpItemDisableListener(this.pluginConfiguration.jumpConfiguration, this.jumpPlayerManager),
                new JumpItemDropListener(this.pluginConfiguration.jumpConfiguration, this.jumpPlayerManager),
                new JumpItemEnableListener(this.pluginConfiguration.jumpConfiguration, this.jumpPlayerManager),
                new JumpItemInteractListener(this.server, this.pluginConfiguration.jumpConfiguration, this.pluginConfiguration.messageConfiguration, this.notificationSender, this.jumpPlayerManager),
                new DoubleJumpListener(this.pluginConfiguration.jumpConfiguration, this.pluginConfiguration.messageConfiguration, this.notificationSender),
                new JumpDisableListener(this.jumpPlayerManager),
                new JumpEnableListener(this.server, this.pluginConfiguration.jumpConfiguration, this.pluginConfiguration.messageConfiguration, this.jumpPlayerManager, this.notificationSender, this.taskScheduler),
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
            String version = plugin.getDescription().getVersion();

            try {
                this.checkForUpdate(version, logger);
            }
            catch (GitException gitException) {
                logger.info(AnsiColor.RED + "An error occurred while checking for update: " + gitException.getMessage() + AnsiColor.RESET);
            }
        }

        Duration timeElapsed = Duration.between(start, Instant.now());
        logger.info("Enabled plugin in " + timeElapsed.toMillis() + "ms.");
    }

    public void disable() {
        DoubleJumpApiProvider.unregister();

        if (this.liteCommands != null) {
            this.liteCommands.getPlatform().unregisterAll();
        }

        this.disableAllowFlightForOnlinePlayers();
        this.bukkitAudiences.close();
    }

    private LiteCommands<CommandSender> registerLiteCommands() {
        return LiteBukkitAdventurePlatformFactory.builder(this.server, "DoubleJump", false, this.bukkitAudiences, true)
                .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>("Only player can use this command."))

                .argument(Player.class, new PlayerArgument(this.server, this.pluginConfiguration.messageConfiguration))

                .permissionHandler(new MissingPermissionHandler(this.pluginConfiguration.messageConfiguration, this.notificationSender))
                .resultHandler(Notification.class, new NotificationHandler(this.notificationSender))
                .invalidUsageHandler(new UsageHandler(this.pluginConfiguration.messageConfiguration, this.notificationSender))

                .commandInstance(
                        new DoubleJumpCommand(this.pluginConfiguration.jumpConfiguration, this.pluginConfiguration.messageConfiguration, this.notificationSender, this.jumpPlayerManager)
                )

                .commandEditor(
                        DoubleJumpCommand.class, new CommandPermissionEditor(this.pluginConfiguration)
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

    private void checkForUpdate(String version, Logger logger) {
        GitCheck gitCheck = new GitCheck();

        GitTag gitTag = GitTag.of("v" + version);
        GitCheckResult checkResult = gitCheck.checkRelease(GIT_REPOSITORY, gitTag);

        if (checkResult.isUpToDate()) {
            logger.info(AnsiColor.GREEN + "You are using latest version. Thank you." + AnsiColor.RESET);
        }
        else {
            GitRelease latestRelease = checkResult.getLatestRelease();

            logger.info(AnsiColor.YELLOW + "A new version is available: " + latestRelease.getTag() + AnsiColor.RESET);
            logger.info(AnsiColor.YELLOW + "Download it here: " + latestRelease.getPageUrl() + AnsiColor.RESET);
        }
    }

    @Nonnull
    @Override
    public JumpPlayerManager getJumpPlayerManager() {
        return this.jumpPlayerManager;
    }
}
