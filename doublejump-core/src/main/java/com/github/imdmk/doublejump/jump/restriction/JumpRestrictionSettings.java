package com.github.imdmk.doublejump.jump.restriction;

import com.github.imdmk.doublejump.notification.Notification;
import com.github.imdmk.doublejump.notification.NotificationType;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.GameMode;

import java.util.List;

public class JumpRestrictionSettings extends OkaeriConfig {

    @Comment({
            "# Names of regions where the player will not be able to double-jump",
            "# The WorldGuard plugin is required for this feature to work"
    })
    public List<String> disabledRegions = List.of(
            "example-region"
    );

    @Comment("# Names of worlds where the player will not be able to double-jump")
    public List<String> disabledWorlds = List.of(
            "example-world"
    );

    @Comment("# The names of the game modes during which the player will not be able to double-jump")
    public List<GameMode> disabledGameModes = List.of(
            GameMode.SPECTATOR,
            GameMode.CREATIVE
    );

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
