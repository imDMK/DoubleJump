package com.github.imdmk.doublejump.jump.command;

import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.jump.JumpPlayerService;
import com.github.imdmk.doublejump.jump.JumpSettings;
import com.github.imdmk.doublejump.jump.restriction.JumpRestrictionService;
import com.github.imdmk.doublejump.notification.NotificationSender;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;

@Route(name = "doublejump")
public class DoubleJumpCommand {

    private final JumpSettings jumpSettings;
    private final NotificationSender notificationSender;
    private final JumpPlayerManager jumpPlayerManager;
    private final JumpPlayerService jumpPlayerService;
    private final JumpRestrictionService jumpRestrictionService;

    public DoubleJumpCommand(JumpSettings jumpSettings, NotificationSender notificationSender, JumpPlayerManager jumpPlayerManager, JumpPlayerService jumpPlayerService, JumpRestrictionService jumpRestrictionService) {
        this.jumpSettings = jumpSettings;
        this.notificationSender = notificationSender;
        this.jumpPlayerManager = jumpPlayerManager;
        this.jumpPlayerService = jumpPlayerService;
        this.jumpRestrictionService = jumpRestrictionService;
    }

    @Execute(required = 0)
    void execute(Player player) {
        if (this.jumpRestrictionService.isPassedRestrictions(player, true)) {
            return;
        }

        if (this.jumpPlayerManager.isDoubleJumpMode(player)) {
            this.jumpPlayerService.disable(player);
            this.notificationSender.send(player, this.jumpSettings.notificationSettings.jumpModeDisabled);
        }
        else {
            this.jumpPlayerService.enable(player, true);
            this.notificationSender.send(player, this.jumpSettings.notificationSettings.jumpModeEnabled);
        }
    }
}
