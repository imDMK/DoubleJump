package com.github.imdmk.doublejump.jump;

import com.github.imdmk.doublejump.jump.item.JumpItemConfiguration;
import com.github.imdmk.doublejump.jump.particle.JumpParticle;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class JumpConfiguration extends OkaeriConfig {

    @Comment("# To auto enable jump mode player must have double jump use permission")
    public boolean enableJumpModeOnJoinForPlayers = true;
    public boolean enableJumpModeOnJoinForAdmins = true;

    public boolean jumpFallDamageEnabled = true;

    @Comment("# Jump streak settings")
    public boolean jumpStreaksEnabled = true;
    public boolean jumpStreakResetOnGround = false;
    public boolean jumpStreakResetOnDeath = true;

    @Comment("# Jump settings")
    public double jumpMultiple = 0.3;
    public double jumpUp = 0.6;

    @Comment({
            "# Jump delay",
            "# Set to 0 to disable"
    })
    public Duration jumpDelay = Duration.ofSeconds(2);

    @Comment("# Jumps limit settings")
    public boolean jumpsLimitEnabled = false;

    @Comment({
            "# After how long should one jump be renewed?",
            "# Set to 0 to disable"
    })
    public Duration jumpsRegenerationDelay = Duration.ofSeconds(3);

    @Comment({
            "# Default jump value",
            "# If the player does not have permissions in the listed jumpsLimitsByPermission will be assigned this value"
    })
    public int jumpsLimit = 1;

    @Comment({
        "# This shows a table with permissions and the jumps counts assigned to them",
        "# If the player has permission X, he will get the number of jumps Y",
        "# Example:",
        "# <PERMISSION: doublejump> <JUMPS: 2>",
        "# If the player has the doublejump permission, he will be assigned the number of jumps 2"
    })
    public Map<String, Integer> jumpsLimitByPermissions = Map.of(
            "doublejump", 2,
            "triplejump", 3,
            "fivejump", 5
    );

    @Comment("# Jump item settings")
    public JumpItemConfiguration jumpItemConfiguration = new JumpItemConfiguration();

    @Comment({
            "# Jump sound settings",
            "# Sound types: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html"
    })
    public boolean jumpSoundsEnabled = true;

    public Sound jumpSound = Sound.ENTITY_EXPERIENCE_BOTTLE_THROW;
    public float jumpSoundVolume = 0.20F;
    public float jumpSoundPitch = 1;

    @Comment("# Jump particles settings")
    public boolean jumpParticlesEnabled = true;

    public int jumpParticlesCount = 3;
    public int jumpParticlesExtra = 2;
    public double jumpParticlesOffsetX = 0;
    public double jumpParticlesOffsetY = 0;
    public double jumpParticlesOffsetZ = 0;

    @Comment("# Color list: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Color.html")
    public List<JumpParticle> jumpParticles = List.of(
            new JumpParticle(Particle.NOTE, Color.WHITE, 20),
            new JumpParticle(Particle.REDSTONE, Color.WHITE, 40)
    );

    @Comment({
            "# Restrictions",
            "# The name of disabled regions where player can't double jump",
            "# NOTE: Required WorldGuard plugin to work"
    })
    public List<String> disabledRegions = List.of(
            "example-region"
    );

    @Comment("# The name of the disabled worlds where players can't double jump")
    public List<String> disabledWorlds = List.of(
            "example-world"
    );

    @Comment("# The name of the game modes where players can't double jump")
    public List<GameMode> disabledGameModes = List.of(
            GameMode.SPECTATOR,
            GameMode.CREATIVE
    );
}
