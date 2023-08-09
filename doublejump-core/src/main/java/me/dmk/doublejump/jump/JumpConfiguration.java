package me.dmk.doublejump.jump;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import me.dmk.doublejump.jump.item.JumpItemConfiguration;
import me.dmk.doublejump.jump.particle.JumpParticle;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

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

    @Comment("# Jump delay settings")
    public boolean jumpDelayEnabled = false;

    public Duration jumpDelay = Duration.ofSeconds(2);

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

    public List<JumpParticle> jumpParticles = new ArrayList<>(List.of(
            new JumpParticle(Particle.NOTE, "WHITE", 20),
            new JumpParticle(Particle.REDSTONE, "WHITE", 40)
    ));

    @Comment("# Restrictions")
    public List<String> disabledRegions = new ArrayList<>(List.of(
            "example-region"
    ));
    public List<String> disabledWorlds = new ArrayList<>(List.of(
            "example-world"
    ));
    public List<GameMode> disabledGameModes = new ArrayList<>(List.of(
            GameMode.SPECTATOR,
            GameMode.CREATIVE
    ));
}
