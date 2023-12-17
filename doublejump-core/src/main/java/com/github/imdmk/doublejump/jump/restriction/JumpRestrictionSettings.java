package com.github.imdmk.doublejump.jump.restriction;

import com.github.imdmk.doublejump.notification.Notification;
import com.github.imdmk.doublejump.notification.NotificationType;
import com.github.imdmk.doublejump.restriction.JumpRestriction;
import com.github.imdmk.doublejump.restriction.JumpRestrictionType;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.GameMode;

import java.util.List;

public class JumpRestrictionSettings extends OkaeriConfig {

    @Comment({
            "# Restriction of regions where the player will not be able to double-jump",
            "# The WorldGuard plugin is required for this feature to work"
    })
    public JumpRestriction<String> regionRestriction = new JumpRestriction<>(JumpRestrictionType.WHITELIST, List.of("example-region"));

    @Comment("# Restriction of worlds where the player will not be able to double-jump")
    public JumpRestriction<String> worldRestriction = new JumpRestriction<>(JumpRestrictionType.BLACKLIST, List.of("example-world"));

    @Comment("# The restriction of the game modes during which the player will not be able to double-jump")
    public JumpRestriction<GameMode> gameModeRestriction = new JumpRestriction<>(JumpRestrictionType.BLACKLIST, List.of(GameMode.CREATIVE));

    @Comment({"#", "# Jump restriction notification settings", "#"})
    public JumpRestrictionNotificationSettings notificationSettings = new JumpRestrictionNotificationSettings();

    public static class JumpRestrictionNotificationSettings extends OkaeriConfig {

        public Notification jumpDisabledRegion = new Notification(NotificationType.CHAT, "<red>You are in disabled region");
        public Notification jumpDisabledGameMode = new Notification(NotificationType.CHAT, "<red>You cannot use double jump on this game mode");
        public Notification jumpDisabledWorld = new Notification(NotificationType.CHAT, "<red>You cannot use double jump in this world");

        public Notification targetInDisabledRegion = new Notification(NotificationType.CHAT, "<red>The player is in disabled region");
        public Notification targetHasDisabledGameMode = new Notification(NotificationType.CHAT, "<red>The player has a game mode that is disabled");
        public Notification targetInDisabledWorld = new Notification(NotificationType.CHAT, "<red>The player is in disabled world");

    }
}
