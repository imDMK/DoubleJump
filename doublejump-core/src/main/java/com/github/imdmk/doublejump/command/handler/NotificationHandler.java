package com.github.imdmk.doublejump.command.handler;

import com.github.imdmk.doublejump.notification.Notification;
import com.github.imdmk.doublejump.notification.NotificationSender;
import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import org.bukkit.command.CommandSender;

public class NotificationHandler implements ResultHandler<CommandSender, Notification> {

    private final NotificationSender notificationSender;

    public NotificationHandler(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    @Override
    public void handle(Invocation<CommandSender> invocation, Notification notification, ResultHandlerChain<CommandSender> resultHandlerChain) {
        CommandSender sender = invocation.sender();

        this.notificationSender.send(sender, notification);
    }
}
