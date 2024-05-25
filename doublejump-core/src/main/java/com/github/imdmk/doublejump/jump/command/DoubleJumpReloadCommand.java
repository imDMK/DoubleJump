package com.github.imdmk.doublejump.jump.command;

import com.github.imdmk.doublejump.configuration.ConfigurationService;
import com.github.imdmk.doublejump.notification.NotificationSender;
import com.github.imdmk.doublejump.notification.configuration.NotificationSettings;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;

@Command(name = "doublejump reload")
@Permission("command.doublejump.reload")
public class DoubleJumpReloadCommand {

    private final NotificationSettings notificationSettings;
    private final NotificationSender notificationSender;
    private final ConfigurationService configurationService;

    public DoubleJumpReloadCommand(NotificationSettings notificationSettings, NotificationSender notificationSender, ConfigurationService configurationService) {
        this.notificationSettings = notificationSettings;
        this.notificationSender = notificationSender;
        this.configurationService = configurationService;
    }

    @Async
    @Execute
    void execute(@Context CommandSender sender) {
        this.configurationService.reload();
        this.notificationSender.send(sender, this.notificationSettings.configurationReloaded);
    }
}
