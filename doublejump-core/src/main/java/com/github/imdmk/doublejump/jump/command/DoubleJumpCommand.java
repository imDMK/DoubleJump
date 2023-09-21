package com.github.imdmk.doublejump.jump.command;

import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.jump.JumpPlayerService;
import com.github.imdmk.doublejump.jump.JumpSettings;
import com.github.imdmk.doublejump.notification.Notification;
import com.github.imdmk.doublejump.notification.NotificationSender;
import com.github.imdmk.doublejump.notification.NotificationSettings;
import com.github.imdmk.doublejump.region.RegionProvider;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@Route(name = "doublejump")
public class DoubleJumpCommand {

    private final JumpSettings jumpSettings;
    private final NotificationSettings notificationSettings;
    private final NotificationSender notificationSender;
    private final JumpPlayerManager jumpPlayerManager;
    private final JumpPlayerService jumpPlayerService;
    private final RegionProvider regionProvider;

    public DoubleJumpCommand(JumpSettings jumpSettings, NotificationSettings notificationSettings, NotificationSender notificationSender, JumpPlayerManager jumpPlayerManager, JumpPlayerService jumpPlayerService, RegionProvider regionProvider) {
        this.jumpSettings = jumpSettings;
        this.notificationSettings = notificationSettings;
        this.notificationSender = notificationSender;
        this.jumpPlayerManager = jumpPlayerManager;
        this.jumpPlayerService = jumpPlayerService;
        this.regionProvider = regionProvider;
    }

    @Execute(required = 0)
    void execute(Player player) {
        GameMode playerGameMode = player.getGameMode();
        String playerWorldName = player.getWorld().getName();

        if (this.regionProvider.isInRegion(player)) {
            this.notificationSender.send(player, this.notificationSettings.targetInDisabledRegionNotification);
            return;
        }

        if (this.jumpSettings.restrictionsSettings.disabledGameModes.contains(playerGameMode)) {
            this.notificationSender.send(player, this.notificationSettings.jumpModeDisabledGameModeNotification);
            return;
        }

        if (this.jumpSettings.restrictionsSettings.disabledWorlds.contains(playerWorldName)) {
            this.notificationSender.send(player, this.notificationSettings.jumpModeDisabledWorldNotification);
            return;
        }

        if (this.jumpPlayerManager.isDoubleJumpMode(player)) {
            this.jumpPlayerService.disable(player);
            this.notificationSender.send(player, this.notificationSettings.jumpModeDisabledNotification);
        }
        else {
            this.jumpPlayerService.enable(player, true);
            this.notificationSender.send(player, this.notificationSettings.jumpModeEnabledNotification);
        }
    }

    @Execute(route = "enable-for", required = 1)
    void enableFor(Player player, @Arg @Name("target") Player target) {
        GameMode targetGameMode = target.getGameMode();
        String targetWorldName = target.getWorld().getName();

        if (this.regionProvider.isInRegion(player)) {
            this.notificationSender.send(player, this.notificationSettings.targetInDisabledRegionNotification);
            return;
        }

        if (this.jumpSettings.restrictionsSettings.disabledGameModes.contains(targetGameMode)) {
            this.notificationSender.send(player, this.notificationSettings.targetHasDisabledGameModeNotification);
            return;
        }

        if (this.jumpSettings.restrictionsSettings.disabledWorlds.contains(targetWorldName)) {
            this.notificationSender.send(player, this.notificationSettings.targetInDisabledWorldNotification);
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
