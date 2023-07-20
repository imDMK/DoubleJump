package me.dmk.doublejump.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import me.dmk.doublejump.notification.Notification;
import me.dmk.doublejump.notification.NotificationType;

public class MessageConfiguration extends OkaeriConfig {

    public Notification playerNotFoundNotification = new Notification(NotificationType.CHAT, "<red>No player found with the given name<dark_gray>.");

    @Comment("# {USAGE} - Correct usage of command")
    public Notification invalidUsageNotification = new Notification(NotificationType.CHAT, "<red>Invalid usage: <dark_red>{USAGE}");
    @Comment("# This messages is used when there is more than one option to use a command")
    public Notification invalidUsageFirstNotification = new Notification(NotificationType.CHAT, "<red>Invalid usage:");
    public Notification invalidUsageListNotification = new Notification(NotificationType.CHAT, "<dark_gray>- <red>{USAGE}");

    @Comment("# {PERMISSIONS} - Required permissions")
    public Notification missingPermissionsNotification = new Notification(NotificationType.CHAT, "<red>Missing permissions: <dark_red>{permissions}<dark_gray>.");

    public Notification jumpModeEnabledNotification = new Notification(NotificationType.CHAT, "<green>Enabled double jump mode!");
    public Notification jumpModeDisabledNotification = new Notification(NotificationType.CHAT, "<red>Disabled double jump mode!");

    @Comment("# {PLAYER} - The name of the player for whom the mode was disabled")
    public Notification jumpModeEnabledForNotification = new Notification(NotificationType.CHAT, "<green>Enabled double jump mode for player {PLAYER}<dark_gray>.");
    public Notification jumpModeDisabledForNotification = new Notification(NotificationType.CHAT, "<red>Disabled double jump mode for player {PLAYER}<dark_gray>.");

    public Notification jumpModeDisabledGameModeNotification = new Notification(NotificationType.CHAT, "<red>U cannot use this command on this game mode.");
    public Notification jumpModeDisabledWorldNotification = new Notification(NotificationType.CHAT, "<red>Double jump has been disabled in this world<dark_gray>.");

    public Notification targetHasDisabledGameModeNotification = new Notification(NotificationType.CHAT, "<red>The player has the game mode disabled<dark_gray>.");
    public Notification targetInDisabledWorldNotification = new Notification(NotificationType.CHAT, "<red>The player is in disabled world<dark_gray>.");

    @Comment("# {STREAK} - Shows current jump streak")
    public Notification jumpStreakIncreaseNotification = new Notification(NotificationType.ACTIONBAR, "<green>Ye! Current streak: <dark_green>{streak}");
    public Notification jumpStreakResetNotification = new Notification(NotificationType.ACTIONBAR, "<red>Ops! Your jump streak has been reset.");
}
