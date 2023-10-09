package com.github.imdmk.doublejump.jump;

import com.github.imdmk.doublejump.jump.item.JumpItemSettings;
import com.github.imdmk.doublejump.jump.particle.JumpParticleSettings;
import com.github.imdmk.doublejump.jump.restriction.JumpRestrictionSettings;
import com.github.imdmk.doublejump.jump.sound.JumpSoundSettings;
import com.github.imdmk.doublejump.notification.Notification;
import com.github.imdmk.doublejump.notification.NotificationType;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

import java.time.Duration;
import java.util.Map;

public class JumpSettings extends OkaeriConfig {

    @Comment({
            "# Specifies whether to enable double jump mode for the player when he joins the server",
            "# NOTE: The player must have double jump permission"
    })
    public boolean enableJumpModeOnJoinForPlayers = true;

    @Comment({
            "# Specifies whether to enable double jump mode for the administrator when he joins the server",
            "# NOTE: The player must have double jump permission and OP"
    })
    public boolean enableJumpModeOnJoinForAdmins = true;

    @Comment("# Whether double jump fall damage is enabled or not")
    public boolean jumpFallDamageEnabled = true;

    @Comment("# Jump multiplier")
    public double jumpMultiple = 0.3;

    @Comment("# The value of how much up the jump is to be made")
    public double jumpUp = 0.6;

    @Comment({"#", "# Jump sound settings", "#"})
    public JumpSoundSettings soundSettings = new JumpSoundSettings();

    @Comment({"#", "# Jump particle settings", "#"})
    public JumpParticleSettings particleSettings = new JumpParticleSettings();

    @Comment({"#", "# Jump restrictions settings", "#"})
    public JumpRestrictionSettings restrictionSettings = new JumpRestrictionSettings();

    @Comment({"#", "# Jump item settings", "#"})
    public JumpItemSettings itemSettings = new JumpItemSettings();

    @Comment({"# ", "# Jump streak settings", "# "})
    public JumpStreakSettings streakSettings = new JumpStreakSettings();

    public static class JumpStreakSettings extends OkaeriConfig {

        @Comment("# Specifies whether to enable double jump series reset")
        public boolean enabled = true;

        @Comment("# Specifies whether the jump streak will be reset when the player touches the ground")
        public boolean resetOnGround = false;

        @Comment("# Specifies whether the jump streak will be reset when the player dies")
        public boolean resetOnDeath = true;

    }

    @Comment({"# ", "# Jump delay settings", "# "})
    public JumpDelaySettings delaySettings = new JumpDelaySettings();

    public static class JumpDelaySettings extends OkaeriConfig {

        @Comment("# Specifies whether double jump delay should be enabled")
        public boolean enabled = true;

        @Comment({
                "# Specifies the double jump delay",
                "# Example value: 1s, 5s, 1m"
        })
        public Duration delay = Duration.ofSeconds(2);

    }

    @Comment({"#", "# Jump limit settings", "#"})
    public JumpLimitSettings limitSettings = new JumpLimitSettings();

    public static class JumpLimitSettings extends OkaeriConfig {

        @Comment("# Specifies whether the jump limit should be enabled")
        public boolean enabled = false;

        @Comment("# The default value of the jump limit is if the player does not have permissions listed in limitsByPermission")
        public int limit = 1;

        @Comment({
                "# Jump limits by permissions",
                "# Example:",
                "# <PERMISSION: double-jump>: <LIMIT: 2>",
                "# If a player has the \"double-jump\" permission, he will be assigned a jump limit of 2",
                "# If not, he will be assigned a default jump limit value"
        })
        public Map<String, Integer> limitsByPermissions = Map.of(
                "double-jump", 2,
                "triple-jump", 3,
                "five-jump", 5
        );

        @Comment({
                "# Specifies the delay after which one jump is to be renewed for the player",
                "# If you want to disable jump regeneration, set the value to 0s"
        })
        public Duration regenerationDelay = Duration.ofSeconds(3);

        @Comment({"#", "# Jump limit notification settings", "#"})
        public JumpLimitNotificationSettings notificationSettings = new JumpLimitNotificationSettings();

        public static class JumpLimitNotificationSettings extends OkaeriConfig {

            public Notification jumpLimit = new Notification(NotificationType.CHAT, "<red>You have no more jumps");
            public Notification jumpLimitReceived = new Notification(NotificationType.CHAT, "<green>You received one jump");

            @Comment("# {TIME} - Time left until get one double jump")
            public Notification jumpLimitRegenerationDelay = new Notification(NotificationType.CHAT, "<red>You must wait {TIME} to get next jump");

        }
    }

    @Comment({"#", "# Jump notification settings", "#"})
    public JumpNotificationSettings notificationSettings = new JumpNotificationSettings();

    public static class JumpNotificationSettings extends OkaeriConfig {

        @Comment("# {TIME} - Time remaining until double jump can be used")
        public Notification jumpDelay = new Notification(NotificationType.CHAT, "<red>You must wait {TIME} before next double jump");

        @Comment("# {STREAK} - New number of jump streak")
        public Notification jumpStreakIncreased = new Notification(NotificationType.ACTIONBAR, "<green>Yeah! Current streak: <dark_green>{STREAK}");
        public Notification jumpStreakHasBeenReset = new Notification(NotificationType.CHAT, "<red>Ops! Your jump streak has been reset");

        public Notification jumpModeEnabled = new Notification(NotificationType.CHAT, "<green>Enabled double jump mode!");
        public Notification jumpModeDisabled = new Notification(NotificationType.CHAT, "<red>Disabled double jump mode!");

        @Comment("# {PLAYER} - The name of the player for whom double jump mode has been enabled")
        public Notification jumpModeEnabledFor = new Notification(NotificationType.CHAT, "<green>Enabled double jump mode for player {PLAYER}");

        @Comment("# {PLAYER} - The name of the player for whom double jump mode has been disabled")
        public Notification jumpModeDisabledFor = new Notification(NotificationType.CHAT, "<red>Disabled double jump mode for player {PLAYER}");

    }
}
