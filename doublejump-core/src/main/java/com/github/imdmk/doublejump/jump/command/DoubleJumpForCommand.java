package com.github.imdmk.doublejump.jump.command;

import com.github.imdmk.doublejump.jump.JumpPlayerService;
import com.github.imdmk.doublejump.jump.restriction.JumpRestrictionService;
import com.github.imdmk.doublejump.notification.Notification;
import com.github.imdmk.doublejump.notification.NotificationSender;
import com.github.imdmk.doublejump.notification.NotificationSettings;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;

@Route(name = "doublejump")
public class DoubleJumpForCommand {

    private final NotificationSettings notificationSettings;
    private final NotificationSender notificationSender;
    private final JumpPlayerService jumpPlayerService;
    private final JumpRestrictionService jumpRestrictionService;

    public DoubleJumpForCommand(NotificationSettings notificationSettings, NotificationSender notificationSender, JumpPlayerService jumpPlayerService, JumpRestrictionService jumpRestrictionService) {
        this.notificationSettings = notificationSettings;
        this.notificationSender = notificationSender;
        this.jumpPlayerService = jumpPlayerService;
        this.jumpRestrictionService = jumpRestrictionService;
    }

    @Execute(route = "enable-for", required = 1)
    void enableFor(Player player, @Arg @Name("target") Player target) {
        if (this.jumpRestrictionService.isPassedRestrictions(player, target, true)) {
            return;
        }

        this.jumpPlayerService.enable(player, true);

        Notification notification = Notification.builder()
                .fromNotification(this.notificationSettings.jumpModeEnabledForNotification)
                .placeholder("{PLAYER}", target.getName())
                .build();

        this.notificationSender.send(player, notification);
    }

    @Execute(route = "disable-for", required = 1)
    void disableFor(Player player, @Arg @Name("target") Player target) {
        this.jumpPlayerService.disable(target);

        Notification notification = Notification.builder()
                .fromNotification(this.notificationSettings.jumpModeDisabledForNotification)
                .placeholder("{PLAYER}", target.getName())
                .build();

        this.notificationSender.send(player, notification);
    }
}
