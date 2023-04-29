package me.dmk.doublejump.command.handler;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.handle.PermissionHandler;
import me.dmk.doublejump.configuration.PluginConfiguration;
import me.dmk.doublejump.notification.Notification;
import me.dmk.doublejump.notification.NotificationSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MissingPermissionHandler implements PermissionHandler<CommandSender> {

    private final PluginConfiguration pluginConfiguration;
    private final NotificationSender notificationSender;

    public MissingPermissionHandler(PluginConfiguration pluginConfiguration, NotificationSender notificationSender) {
        this.pluginConfiguration = pluginConfiguration;
        this.notificationSender = notificationSender;
    }

    @Override
    public void handle(CommandSender commandSender, LiteInvocation liteInvocation, RequiredPermissions requiredPermissions) {
        Notification notification = this.pluginConfiguration.getmissingPermissionNotification();
        String message = notification.getMessage()
                .replace("{permissions}", String.join(", ", requiredPermissions.getPermissions()));

        if (commandSender instanceof Player player) {
            this.notificationSender.sendMessage(player, message);
        } else {
            commandSender.sendMessage(message);
        }
    }
}
