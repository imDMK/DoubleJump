package me.dmk.doublejump.command.handler;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.handle.PermissionHandler;
import me.dmk.doublejump.configuration.PluginConfiguration;
import me.dmk.doublejump.notification.NotificationBuilder;
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
        NotificationBuilder notificationBuilder = this.notificationSender.builder()
                .fromNotification(this.pluginConfiguration.missingPermissionNotification)
                .placeholder("{permissions}", requiredPermissions.getPermissions());

        if (commandSender instanceof Player player) {
            notificationBuilder.send(player);
        }
        else {
            notificationBuilder.send(commandSender);
        }
    }
}
