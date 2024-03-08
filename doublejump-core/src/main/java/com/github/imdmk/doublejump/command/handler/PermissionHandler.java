package com.github.imdmk.doublejump.command.handler;

import com.github.imdmk.doublejump.notification.NotificationSender;
import com.github.imdmk.doublejump.notification.configuration.NotificationSettings;
import com.github.imdmk.doublejump.text.Formatter;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import org.bukkit.command.CommandSender;

public class PermissionHandler implements MissingPermissionsHandler<CommandSender> {

    private final NotificationSettings notificationSettings;
    private final NotificationSender notificationSender;

    public PermissionHandler(NotificationSettings notificationSettings, NotificationSender notificationSender) {
        this.notificationSettings = notificationSettings;
        this.notificationSender = notificationSender;
    }

    @Override
    public void handle(Invocation<CommandSender> invocation, MissingPermissions missingPermissions, ResultHandlerChain<CommandSender> resultHandlerChain) {
        CommandSender sender = invocation.sender();
        Formatter formatter = new Formatter()
                .placeholder("{PERMISSIONS}", missingPermissions.getPermissions());

        this.notificationSender.send(sender, this.notificationSettings.missingPermissions, formatter);
    }
}
