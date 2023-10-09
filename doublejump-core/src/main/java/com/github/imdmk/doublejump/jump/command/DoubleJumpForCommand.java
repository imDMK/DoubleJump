package com.github.imdmk.doublejump.jump.command;

import com.github.imdmk.doublejump.jump.JumpPlayerService;
import com.github.imdmk.doublejump.jump.JumpSettings;
import com.github.imdmk.doublejump.jump.restriction.JumpRestrictionService;
import com.github.imdmk.doublejump.notification.NotificationSender;
import com.github.imdmk.doublejump.text.Formatter;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;

@Route(name = "doublejump")
public class DoubleJumpForCommand {

    private final JumpSettings jumpSettings;
    private final NotificationSender notificationSender;
    private final JumpPlayerService jumpPlayerService;
    private final JumpRestrictionService jumpRestrictionService;

    public DoubleJumpForCommand(JumpSettings jumpSettings, NotificationSender notificationSender, JumpPlayerService jumpPlayerService, JumpRestrictionService jumpRestrictionService) {
        this.jumpSettings = jumpSettings;
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

        Formatter formatter = new Formatter()
                .placeholder("{PLAYER}", target.getName());

        this.notificationSender.send(player, this.jumpSettings.notificationSettings.jumpModeEnabledFor, formatter);
    }

    @Execute(route = "disable-for", required = 1)
    void disableFor(Player player, @Arg @Name("target") Player target) {
        this.jumpPlayerService.disable(target);

        Formatter formatter = new Formatter()
                .placeholder("{PLAYER}", target.getName());

        this.notificationSender.send(player, this.jumpSettings.notificationSettings.jumpModeDisabledFor, formatter);
    }
}
