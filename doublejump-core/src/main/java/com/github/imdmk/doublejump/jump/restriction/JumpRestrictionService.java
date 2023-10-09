package com.github.imdmk.doublejump.jump.restriction;

import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.JumpSettings;
import com.github.imdmk.doublejump.notification.Notification;
import com.github.imdmk.doublejump.notification.NotificationSender;
import com.github.imdmk.doublejump.notification.NotificationSettings;
import com.github.imdmk.doublejump.region.RegionProvider;
import com.github.imdmk.doublejump.text.Formatter;
import com.github.imdmk.doublejump.util.DurationUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class JumpRestrictionService {

    private final JumpSettings jumpSettings;
    private final JumpRestrictionSettings restrictionSettings;
    private final NotificationSettings notificationSettings;
    private final RegionProvider regionProvider;
    private final NotificationSender notificationSender;

    public JumpRestrictionService(JumpSettings jumpSettings, JumpRestrictionSettings restrictionSettings, NotificationSettings notificationSettings, RegionProvider regionProvider, NotificationSender notificationSender) {
        this.jumpSettings = jumpSettings;
        this.restrictionSettings = restrictionSettings;
        this.notificationSettings = notificationSettings;
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
            this.sendNotification(player, this.notificationSettings.jumpDelayNotification, formatter, sendNotification);

            return true;
        }

        if (!jumpPlayer.hasJumps()) {
            if (jumpPlayer.isJumpsNotificationReceived()) {
                return true;
            }

            if (this.jumpSettings.limitSettings.regenerationDelay.isZero()) {
                jumpPlayer.setJumpsNotificationReceived(true);
                this.sendNotification(player, this.notificationSettings.jumpLimitNotification, sendNotification);

                return true;
            }

            Formatter formatter = new Formatter()
                    .placeholder("{TIME}", DurationUtil.toHumanReadable(jumpPlayer.getRemainingJumpRegenerationDuration()));

            jumpPlayer.setJumpsNotificationReceived(true);
            this.sendNotification(player, this.notificationSettings.jumpLimitDelayNotification, formatter, sendNotification);

            return true;
        }

        jumpPlayer.setDelayNotificationReceived(false);
        jumpPlayer.setJumpsNotificationReceived(false);

        return false;
    }

    public boolean isPassedRestrictions(Player player, boolean sendNotification) {
        if (this.regionProvider.isInRegion(player)) {
            this.sendNotification(player, this.notificationSettings.jumpModeDisableRegionNotification, sendNotification);
            return true;
        }

        GameMode playerGameMode = player.getGameMode();
        if (this.restrictionSettings.disabledGameModes.contains(playerGameMode)) {
            this.sendNotification(player, this.notificationSettings.jumpModeDisabledGameModeNotification, sendNotification);
            return true;
        }

        String playerWorldName = player.getWorld().getName();
        if (this.restrictionSettings.disabledWorlds.contains(playerWorldName)) {
            this.sendNotification(player, this.notificationSettings.jumpModeDisabledWorldNotification, sendNotification);
            return true;
        }

        return false;
    }

    public boolean isPassedRestrictions(Player player, Player target, boolean sendNotification) {
        if (this.regionProvider.isInRegion(target)) {
            this.sendNotification(player, this.notificationSettings.targetInDisabledRegionNotification, sendNotification);
            return true;
        }

        GameMode targetGameMode = target.getGameMode();
        if (this.restrictionSettings.disabledGameModes.contains(targetGameMode)) {
            this.sendNotification(player, this.notificationSettings.targetHasDisabledGameModeNotification, sendNotification);
            return true;
        }

        String targetWorldName = target.getWorld().getName();
        if (this.restrictionSettings.disabledWorlds.contains(targetWorldName)) {
            this.sendNotification(player, this.notificationSettings.targetInDisabledWorldNotification, sendNotification);
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
