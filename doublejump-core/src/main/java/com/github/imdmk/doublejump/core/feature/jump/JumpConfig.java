package com.github.imdmk.doublejump.core.feature.jump;

import com.github.imdmk.doublejump.core.config.ConfigSection;
import com.github.imdmk.doublejump.core.config.serializer.ParticleSerializer;
import com.github.imdmk.doublejump.core.config.serializer.SoundSerializer;
import com.github.imdmk.doublejump.core.feature.jump.effect.JumpEffect;
import com.github.imdmk.doublejump.core.feature.jump.effect.JumpEffectSerializer;
import com.github.imdmk.doublejump.core.feature.jump.effect.ParticleEffect;
import com.github.imdmk.doublejump.core.feature.jump.effect.SoundEffect;
import com.github.imdmk.doublejump.core.feature.jump.velocity.JumpVelocity;
import com.github.imdmk.doublejump.core.feature.jump.velocity.JumpVelocitySerializer;
import com.github.imdmk.doublejump.core.injector.annotations.ConfigFile;
import com.github.imdmk.doublejump.core.shared.permission.PermissionValueConfig;
import com.github.imdmk.doublejump.core.shared.permission.PermissionValueConfigSerializer;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ConfigFile
public final class JumpConfig extends ConfigSection {

    @Comment({"#", "# Should the double jump fall damage be enabled?", "#"})
    public boolean enableFallDamage = false;

    @Comment({"#", "# Should the double jump mode be disabled after death?", "#"})
    public boolean disableAfterDeath = false;

    @Comment({
            "#",
            "# Should players have double jump enabled on join?",
            "# Permissions can override this value.",
            "#"
    })
    public PermissionValueConfig<Boolean> enableOnJoin = new PermissionValueConfig<>(
            false,
            Map.of("doublejump.vip", false)
    );

    @Comment({
            "#",
            "# Controls jump velocity (horizontal and vertical boost).",
            "# Can be overridden with permissions.",
            "#"
    })
    public PermissionValueConfig<JumpVelocity> velocities = new PermissionValueConfig<>(
            JumpVelocity.of(0.9, 0.35),
            Map.of(
                    "doublejump.vip", JumpVelocity.of(1, 0.35)
            )
    );

    @Comment({
            "#",
            "# Cooldown between jumps.",
            "# Supports permission-based overrides.",
            "#"
    })
    public PermissionValueConfig<Duration> cooldowns = new PermissionValueConfig<>(
            Duration.ofSeconds(3),
            Map.of(
                    "doublejump.vip", Duration.ofSeconds(2)
            )
    );

    @Comment({
            "#",
            "# Visual effects played when player performs a double jump.",
            "# Supports permission-based overrides.",
            "# You can define multiple effects (particles, sounds, etc.).",
            "#"
    })
    public PermissionValueConfig<List<JumpEffect>> effects = new PermissionValueConfig<>(
            List.of(
                    new ParticleEffect(Particle.CLOUD, 10, 0.3, 0.01),
                    new ParticleEffect(Particle.END_ROD, 5, 0.3, 0.01),
                    new SoundEffect(Sound.ENTITY_BREEZE_JUMP, 0.6f, 1.2f)
            ),
            Map.of(
                    "doublejump.vip", List.of(
                            new ParticleEffect(Particle.FLAME, 15, 0.3, 0.01),
                            new ParticleEffect(Particle.SOUL_FIRE_FLAME, 10, 0.3, 0.01),
                            new ParticleEffect(Particle.END_ROD, 10, 0.3, 0.01),
                            new SoundEffect(Sound.ENTITY_BLAZE_SHOOT, 0.8f, 1.0f)
                    )
            )
    );

    @Comment({"#", "# Worlds where double jump is blocked.", "#"})
    public Set<String> blockedWorlds = Set.of(
            "example-world"
    );

    @Comment({"#", "# Regions where double jump is blocked.", "# Requires WorldGuard plugin.", "#"})
    public Set<String> blockedRegions = Set.of(
            "example-region"
    );

    @Comment({"#", "# Game modes where double jump is disabled.", "#"})
    public Set<GameMode> blockedGameModes = Set.of(
            GameMode.CREATIVE, GameMode.ADVENTURE
    );

    @Comment({"#", "# Block double jump when player is in combat", "# Requires EternalCombat plugin.", "#"})
    public boolean blockInCombat = true;

    @Comment({"#", "# Block double jump while player is gliding (elytra).", "#"})
    public boolean blockWhileGliding = true;

    @Comment({"#", "# Block double jump when player is lagging.", "#"})
    public boolean blockWhenLagging = true;

    @Comment({"#", "# Block double jump while player is in fluid (water/lava).", "#"})
    public boolean blockInFluid = true;

    @Comment({"#", "# Block double jump while player is inside a vehicle.", "#"})
    public boolean blockInVehicle = true;

    @Override
    public OkaeriSerdesPack serdesPack() {
        return registry -> {
            registry.register(new JumpVelocitySerializer());
            registry.register(new JumpEffectSerializer());
            registry.register(new SoundSerializer());
            registry.register(new ParticleSerializer());

            registry.register(new PermissionValueConfigSerializer<JumpVelocity>());
            registry.register(new PermissionValueConfigSerializer<Duration>());
            registry.register(new PermissionValueConfigSerializer<List<JumpEffect>>());
        };
    }

    @Override
    public String fileName() {
        return "jumpConfig.yml";
    }
}