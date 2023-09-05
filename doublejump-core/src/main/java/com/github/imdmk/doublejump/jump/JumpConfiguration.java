package com.github.imdmk.doublejump.jump;

import com.github.imdmk.doublejump.jump.item.configuration.JumpItemConfiguration;
import com.github.imdmk.doublejump.jump.particle.JumpParticleConfiguration;
import com.github.imdmk.doublejump.jump.sound.JumpSoundConfiguration;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.GameMode;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class JumpConfiguration extends OkaeriConfig {

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

    @Comment({
            "# ",
            "# Jump streak configuration",
            "# "
    })
    public StreakConfiguration streakConfiguration = new StreakConfiguration();

    public static class StreakConfiguration extends OkaeriConfig {

        @Comment("# Specifies whether to enable double jump series reset")
        public boolean enabled = true;

        @Comment("# Specifies whether the jump streak will be reset when the player touches the ground")
        public boolean resetOnGround = false;

        @Comment("# Specifies whether the jump streak will be reset when the player dies")
        public boolean resetOnDeath = true;

    }

    @Comment({
            "# ",
            "# Jump delay configuration",
            "# "
    })
    public DelayConfiguration delayConfiguration = new DelayConfiguration();

    public static class DelayConfiguration extends OkaeriConfig {

        @Comment("# Specifies whether double jump delay should be enabled")
        public boolean enabled = true;

        @Comment({
                "# Specifies the double jump delay",
                "# Example value: 1s, 5s, 1m"
        })
        public Duration delay = Duration.ofSeconds(2);

    }

    @Comment({
            "# ",
            "# Jump limit configuration",
            "# "
    })
    public LimitConfiguration limitConfiguration = new LimitConfiguration();

    public static class LimitConfiguration extends OkaeriConfig {

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

    }

    @Comment({
            "# ",
            "# Jump sound configuration",
            "# "
    })
    public JumpSoundConfiguration soundConfiguration = new JumpSoundConfiguration();

    @Comment({
            "# ",
            "# Jump particle configuration",
            "# "
    })
    public JumpParticleConfiguration particleConfiguration = new JumpParticleConfiguration();

    @Comment({
            "# ",
            "# Jump restrictions configuration",
            "# "
    })
    public RestrictionsConfiguration restrictionsConfiguration = new RestrictionsConfiguration();

    public static class RestrictionsConfiguration extends OkaeriConfig {

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

    }

    @Comment({
            "# ",
            "# Jump item configuration",
            "# "
    })
    public JumpItemConfiguration itemConfiguration = new JumpItemConfiguration();
}
