package me.dmk.doublejump.command;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import me.dmk.doublejump.configuration.PluginConfiguration;
import me.dmk.doublejump.notification.Notification;
import me.dmk.doublejump.notification.NotificationSender;
import me.dmk.doublejump.player.JumpPlayerManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@Route(name = "doublejump")
public class DoubleJumpCommand {

    private final PluginConfiguration pluginConfiguration;
    private final NotificationSender notificationSender;
    private final JumpPlayerManager jumpPlayerManager;

    public DoubleJumpCommand(PluginConfiguration pluginConfiguration, NotificationSender notificationSender, JumpPlayerManager jumpPlayerManager) {
        this.pluginConfiguration = pluginConfiguration;
        this.notificationSender = notificationSender;
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @Async
    @Execute(required = 0)
    void execute(Player player) {
        GameMode playerGameMode = player.getGameMode();
        String playerWorldName = player.getWorld().getName();

        if (this.pluginConfiguration.disabledGameModes.contains(playerGameMode)) {
            this.notificationSender.sendMessage(player, this.pluginConfiguration.jumpModeDisabledGameModeNotification);
            return;
        }

        if (this.pluginConfiguration.disabledWorlds.contains(playerWorldName)) {
            this.notificationSender.sendMessage(player, this.pluginConfiguration.jumpModeDisabledWorldNotification);
            return;
        }

        if (this.jumpPlayerManager.isDoubleJumpMode(player)) {
            this.jumpPlayerManager.disable(player);
            this.notificationSender.sendMessage(player, this.pluginConfiguration.jumpModeDisabledNotification);
        }
        else {
            this.jumpPlayerManager.enable(player, true);
            this.notificationSender.sendMessage(player, this.pluginConfiguration.jumpModeEnabledNotification);
        }
    }

    @Async
    @Execute(route = "enable-for", required = 1)
    void enableFor(Player player, @Arg @Name("target") Player target) {
        GameMode targetGameMode = target.getGameMode();
        String targetWorldName = target.getWorld().getName();

        if (this.pluginConfiguration.disabledGameModes.contains(targetGameMode)) {
            this.notificationSender.sendMessage(player, this.pluginConfiguration.targetHasDisabledGameModeNotification);
            return;
        }

        if (this.pluginConfiguration.disabledWorlds.contains(targetWorldName)) {
            this.notificationSender.sendMessage(player, this.pluginConfiguration.targetInDisabledWorldNotification);
            return;
        }

        this.jumpPlayerManager.enable(player, true);

        Notification notification = Notification.builder()
                .fromNotification(this.pluginConfiguration.jumpModeEnabledForNotification)
                .placeholder("{PLAYER}", target.getName())
                .build();

        this.notificationSender.sendMessage(player, notification);
    }

    @Async
    @Execute(route = "disable-for", required = 1)
    void disableFor(Player player, @Arg @Name("target") Player target) {
        this.jumpPlayerManager.disable(target);

        Notification notification = Notification.builder()
                .fromNotification(this.pluginConfiguration.jumpModeDisabledForNotification)
                .placeholder("{PLAYER}", target.getName())
                .build();

        this.notificationSender.sendMessage(player, notification);
    }
}
