package com.github.imdmk.doublejump.feature.jump;

import com.github.imdmk.doublejump.configuration.ConfigSection;
import com.github.imdmk.doublejump.configuration.serializer.ColorSerializer;
import com.github.imdmk.doublejump.configuration.serializer.ComponentSerializer;
import com.github.imdmk.doublejump.configuration.serializer.EnchantmentSerializer;
import com.github.imdmk.doublejump.feature.jump.item.configuration.JumpItemConfiguration;
import com.github.imdmk.doublejump.feature.jump.item.configuration.JumpItemSerializer;
import com.github.imdmk.doublejump.feature.jump.particle.JumpParticle;
import com.github.imdmk.doublejump.feature.jump.particle.JumpParticleSerializer;
import com.github.imdmk.doublejump.feature.jump.sound.JumpSound;
import com.github.imdmk.doublejump.feature.jump.sound.configuration.JumpSoundSerializer;
import com.github.imdmk.doublejump.feature.jump.sound.configuration.SoundSerializer;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class JumpConfiguration extends ConfigSection {

    @Comment("Enables double jump automatically for players when they join the server.")
    public boolean autoEnableOnJoin = true;

    @Comment("# Permission required to auto-enable double jump on join.")
    public String autoEnablePermission = "doublejump.join";

    @Comment("# Auto-enables double jump for players with OP or high permission level.")
    public boolean autoEnableForAdmins = false;

    @Comment("# Determines whether players will receive fall damage after double jumping.")
    public boolean applyFallDamage = true;

    @Comment("# Horizontal boost multiplier applied during a double jump.")
    public double horizontalBoost = 0.6;

    @Comment("# Vertical boost height applied during a double jump.")
    public double verticalBoost = 0.9;

    @Comment("# Jump delay that player must wait before next double jump. Set to 0s to disable this feature.")
    public Duration jumpDelay = Duration.ofSeconds(3L);

    @Comment("# Restrictions that limit or conditionally disable double jumping.")
    public JumpRestrictionConfiguration restrictions = new JumpRestrictionConfiguration();

    public static class JumpRestrictionConfiguration extends OkaeriConfig {

        @Comment("# List of worlds where double jumping is disabled. Leave empty if disable this feature.")
        public Set<String> disabledWorlds = Set.of("world1");

        @Comment("# List of game modes where double jumping is disabled. Leave empty if disable this feature.")
        public Set<GameMode> disabledGameModes = Set.of(GameMode.CREATIVE, GameMode.ADVENTURE);

        @Comment("# List of permissions that allow double jumping. Players must have at least one. Leave empty if disable this feature.")
        public Set<String> allowedPermissions = Set.of();

        @Comment("# Disable double jumping if the player is lagging (e.g., high ping). We recommended this value set to true to avoid player flying.")
        public boolean disableIfPlayerLagging = true;

    }

    @Comment("# Configuration for visual particle effects triggered by double jump.")
    public JumpParticleConfiguration particles = new JumpParticleConfiguration();

    public static class JumpParticleConfiguration extends OkaeriConfig {

        @Comment("# Enables or disables the double jump particle effect.")
        public boolean enabled = true;

        @Comment("List of particle effect spawned when a player uses double jump.")
        public List<JumpParticle> jump = List.of(
                JumpParticle.builder(Particle.HEART, 10)
                        .offsetX(0.5)
                        .offsetY(1)
                        .offsetZ(0.5)
                        .extra(0)
                        .build()
        );
    }

    @Comment("# Configuration for sounds triggered by double jump.")
    public JumpSoundConfiguration sounds = new JumpSoundConfiguration();

    public static class JumpSoundConfiguration extends OkaeriConfig {

        @Comment("# Enables or disables the double jump sounds.")
        public boolean enabled = true;

        @Comment("List of sounds played when a player uses double jump.")
        public List<JumpSound> jump = List.of(
                JumpSound.builder()
                        .sound(Sound.ENTITY_PLAYER_LEVELUP)
                        .volume(0.3F)
                        .pitch(0.5F)
                        .build()
        );
    }

    @Comment("# Configuration for the item that activates the double jump ability.")
    public JumpItemConfiguration jumpItem = new JumpItemConfiguration();

    @Override
    public @NotNull OkaeriSerdesPack getSerdesPack() {
        return registry -> {
            registry.register(new SerdesCommons());
            registry.register(new ComponentSerializer());

            registry.register(new ColorSerializer());
            registry.register(new JumpParticleSerializer());

            registry.register(new JumpSoundSerializer());
            registry.register(new SoundSerializer());

            registry.register(new JumpItemSerializer());
            registry.register(new EnchantmentSerializer());
        };
    }

    @Override
    public @NotNull String getFileName() {
        return "jumpConfiguration.yml";
    }
}
