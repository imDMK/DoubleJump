package me.dmk.doublejump.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import me.dmk.doublejump.particle.JumpParticle;
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

    @Comment("# NOTE: It is not guaranteed that the player will receive fall damage")
    public boolean jumpFallDamageEnabled = true;
    public boolean jumpParticlesEnabled = true;
    public boolean jumpSoundsEnabled = true;
    public boolean jumpDelayEnabled = true;

    public boolean jumpStreaksEnabled = true;
    public boolean jumpStreakResetOnGround = false;
    public boolean jumpStreakResetOnDeath = true;

    public double jumpMultiple = 0.5;
    public double jumpUp = 0.5;

    public Duration jumpDelay = Duration.ofSeconds(2);

    @Comment({
            "# Jump sound",
            "# Sound types: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html"
    })
    public Sound jumpSound = Sound.ENTITY_EXPERIENCE_BOTTLE_THROW;
    public float jumpSoundVolume = 0.20F;
    public float jumpSoundPitch = 1;

    @Comment("# Jump particles")
    public int jumpParticlesCount = 10;
    public int jumpParticlesExtra = 0;
    public double jumpParticlesOffsetX = 0;
    public double jumpParticlesOffsetY = 0;
    public double jumpParticlesOffsetZ = 0;

    public List<JumpParticle> jumpParticles = new ArrayList<>(List.of(
            new JumpParticle(Particle.NOTE, "AQUA", 20),
            new JumpParticle(Particle.REDSTONE, "RED", 40)
    ));

    @Comment("# Restrictions")
    public List<String> disabledWorlds = new ArrayList<>();
    public List<GameMode> disabledGameModes = new ArrayList<>(List.of(
            GameMode.SPECTATOR,
            GameMode.CREATIVE
    ));
}
