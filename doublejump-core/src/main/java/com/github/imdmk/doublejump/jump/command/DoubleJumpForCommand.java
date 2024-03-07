package com.github.imdmk.doublejump.jump.command;

import com.github.imdmk.doublejump.jump.JumpPlayerService;
import com.github.imdmk.doublejump.jump.JumpSettings;
import com.github.imdmk.doublejump.notification.NotificationSender;
import com.github.imdmk.doublejump.text.Formatter;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "doublejump for")
@Permission("command.doublejump.for")
public class DoubleJumpForCommand {

    private final JumpSettings jumpSettings;
    private final NotificationSender notificationSender;
    private final JumpPlayerService jumpPlayerService;

    public DoubleJumpForCommand(JumpSettings jumpSettings, NotificationSender notificationSender, JumpPlayerService jumpPlayerService) {
        this.jumpSettings = jumpSettings;
        this.notificationSender = notificationSender;
        this.jumpPlayerService = jumpPlayerService;
    }

    @Execute(name = "enable")
    void enableFor(@Context CommandSender sender, @Arg("target") Player target) {
        this.jumpPlayerService.enable(target, true);

        Formatter formatter = new Formatter()
                .placeholder("{PLAYER}", target.getName());

        this.notificationSender.send(sender, this.jumpSettings.notificationSettings.jumpModeEnabledFor, formatter);
    }

    @Execute(name = "disable")
    void disableFor(@Context CommandSender sender, @Arg("target") Player target) {
        this.jumpPlayerService.disable(target);

        Formatter formatter = new Formatter()
                .placeholder("{PLAYER}", target.getName());

        this.notificationSender.send(sender, this.jumpSettings.notificationSettings.jumpModeDisabledFor, formatter);
    }
}
