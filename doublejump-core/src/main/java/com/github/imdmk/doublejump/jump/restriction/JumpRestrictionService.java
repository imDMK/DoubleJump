package com.github.imdmk.doublejump.jump.restriction;

import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.JumpSettings;
import com.github.imdmk.doublejump.notification.Notification;
import com.github.imdmk.doublejump.notification.NotificationSender;
import com.github.imdmk.doublejump.region.RegionProvider;
import com.github.imdmk.doublejump.text.Formatter;
import com.github.imdmk.doublejump.util.DurationUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class JumpRestrictionService {

    private final JumpSettings jumpSettings;
    private final JumpRestrictionSettings restrictionSettings;
    private final RegionProvider regionProvider;
    private final NotificationSender notificationSender;

    public JumpRestrictionService(JumpSettings jumpSettings, JumpRestrictionSettings restrictionSettings, RegionProvider regionProvider, NotificationSender notificationSender) {
        this.jumpSettings = jumpSettings;
        this.restrictionSettings = restrictionSettings;
        this.regionProvider = regionProvider;
        this.notificationSender = notificationSender;
    }

    public boolean isPassedRestrictions(Player player, JumpPlayer jumpPlayer, boolean sendNotification) {
        if (jumpPlayer.isDelay()) {
            if (jumpPlayer.isDelayNotificationReceived()) {
                return true;
            }

            Formatter formatter = new Formatter()
                    .placeholder("{TIME}", DurationUtil.toHumanReadable(jumpPlayer.getRemainingDelayDuration()));

            jumpPlayer.setDelayNotificationReceived(true);
            this.sendNotification(player, this.jumpSettings.notificationSettings.jumpDelay, formatter, sendNotification);

            return true;
        }

        if (!jumpPlayer.hasJumps()) {
            if (jumpPlayer.isJumpsNotificationReceived()) {
                return true;
            }

            if (this.jumpSettings.limitSettings.regenerationDelay.isZero()) {
                jumpPlayer.setJumpsNotificationReceived(true);
                this.sendNotification(player, this.jumpSettings.limitSettings.notificationSettings.jumpLimit, sendNotification);

                return true;
            }

            Formatter formatter = new Formatter()
                    .placeholder("{TIME}", DurationUtil.toHumanReadable(jumpPlayer.getRemainingJumpRegenerationDuration()));

            jumpPlayer.setJumpsNotificationReceived(true);
            this.sendNotification(player, this.jumpSettings.limitSettings.notificationSettings.jumpLimitRegenerationDelay, formatter, sendNotification);

            return true;
        }

        jumpPlayer.setDelayNotificationReceived(false);
        jumpPlayer.setJumpsNotificationReceived(false);

        return false;
    }

    public boolean isPassedRestrictions(Player player, boolean sendNotification) {
        if (!this.regionProvider.isInAllowedRegion(player)) {
            this.sendNotification(player, this.restrictionSettings.notificationSettings.jumpDisabledRegion, sendNotification);
            return true;
        }

        GameMode playerGameMode = player.getGameMode();
        if (!this.restrictionSettings.gameModeRestriction.isAllowed(playerGameMode.name())) {
            this.sendNotification(player, this.restrictionSettings.notificationSettings.jumpDisabledGameMode, sendNotification);
            return true;
        }

        String playerWorldName = player.getWorld().getName();
        if (!this.restrictionSettings.worldRestriction.isAllowed(playerWorldName)) {
            this.sendNotification(player, this.restrictionSettings.notificationSettings.jumpDisabledWorld, sendNotification);
            return true;
        }

        return false;
    }

    public boolean isPassedRestrictions(Player player, Player target, boolean sendNotification) {
        if (!this.regionProvider.isInAllowedRegion(target)) {
            this.sendNotification(player, this.restrictionSettings.notificationSettings.targetInDisabledRegion, sendNotification);
            return true;
        }

        GameMode targetGameMode = target.getGameMode();
        if (!this.restrictionSettings.gameModeRestriction.isAllowed(targetGameMode.name())) {
            this.sendNotification(player, this.restrictionSettings.notificationSettings.targetHasDisabledGameMode, sendNotification);
            return true;
        }

        String targetWorldName = target.getWorld().getName();
        if (!this.restrictionSettings.worldRestriction.isAllowed(targetWorldName)) {
            this.sendNotification(player, this.restrictionSettings.notificationSettings.targetInDisabledWorld, sendNotification);
            return true;
        }

        return false;
    }

    private void sendNotification(Player player, Notification notification, boolean send) {
        if (send) {
            this.notificationSender.send(player, notification);
        }
    }

    private void sendNotification(Player player, Notification notification, Formatter formatter, boolean send) {
        if (send) {
            this.notificationSender.send(player, notification, formatter);
        }
    }
}
