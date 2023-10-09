package com.github.imdmk.doublejump.notification.configuration;

import com.github.imdmk.doublejump.notification.Notification;
import com.github.imdmk.doublejump.notification.NotificationType;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

public class NotificationSettings extends OkaeriConfig {

    public Notification playerNotFound = new Notification(NotificationType.CHAT, "<red>No player found with the given name");

    @Comment("# {PERMISSIONS} - Required permissions to use the command")
    public Notification missingPermissions = new Notification(NotificationType.CHAT, "<red>Missing permissions: <dark_red>{PERMISSIONS}");

    @Comment("# {USAGE} - Correct use of the command")
    public Notification invalidUsage = new Notification(NotificationType.CHAT, "<red>Invalid usage: <dark_red>{USAGE}");

    @Comment("# Used when there is more than one option for using a command")
    public Notification invalidUsageListFirst = new Notification(NotificationType.CHAT, "<red>Invalid usage:");

    @Comment({
            "# {USAGE} - Correct use of the command",
            "# Used when there is more than one option for using a command"
    })
    public Notification invalidUsageList = new Notification(NotificationType.CHAT, "<dark_gray>- <red>{USAGE}");
}
