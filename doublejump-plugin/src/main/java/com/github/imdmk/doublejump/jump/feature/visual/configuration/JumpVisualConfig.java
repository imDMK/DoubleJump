package com.github.imdmk.doublejump.jump.feature.visual.configuration;

import com.github.imdmk.doublejump.config.ConfigSection;
import com.github.imdmk.doublejump.jump.feature.visual.JumpVisual;
import com.github.imdmk.doublejump.jump.feature.visual.particle.JumpParticle;
import com.github.imdmk.doublejump.jump.feature.visual.particle.configuration.JumpParticleConfiguration;
import com.github.imdmk.doublejump.jump.feature.visual.particle.configuration.JumpParticleSerializer;
import com.github.imdmk.doublejump.jump.feature.visual.particle.gui.JumpParticleEntrySerializer;
import com.github.imdmk.doublejump.jump.feature.visual.sound.JumpSound;
import com.github.imdmk.doublejump.jump.feature.visual.sound.configuration.JumpSoundConfiguration;
import com.github.imdmk.doublejump.jump.feature.visual.sound.configuration.JumpSoundSerializer;
import com.github.imdmk.doublejump.jump.feature.visual.sound.configuration.SoundSerializer;
import com.github.imdmk.doublejump.jump.feature.visual.sound.gui.JumpSoundEntrySerializer;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Header({
        "# ",
        "# DoubleJump Premium - Visual Effects Configuration",
        "# Configure default and custom visual and sound effects",
        "# displayed when players perform a double jump.",
        "# ",
        "# Particles and sounds can be customized per player or globally.",
        "# ",
        "# Enjoying the plugin? Please leave a review on SpigotMC!",
        "# Support development: https://github.com/sponsors/imDMK",
        "# "
})
public class JumpVisualConfig extends ConfigSection {

    @Comment({
            "# Default visual settings applied to players without custom visuals.",
            "# Set to 'null' to disable all visuals by default.",
            "# Typically used on player join or when resetting visuals."
    })
    public JumpVisual defaultVisuals = new JumpVisual(
            List.of(new JumpParticle(Particle.HEART, 10)),
            new JumpSound(Sound.ENTITY_PLAYER_LEVELUP, 0.3F, 0.5F)
    );

    @Comment("# Configuration for particle effects displayed on double jump")
    public JumpParticleConfiguration particles = new JumpParticleConfiguration();

    @Comment("# Configuration for sound effects played on double jump")
    public JumpSoundConfiguration sounds = new JumpSoundConfiguration();

    @Override
    public @NotNull OkaeriSerdesPack getSerdesPack() {
        return registry -> {
            registry.register(new JumpVisualSerializer());

            registry.register(new JumpParticleSerializer());
            registry.register(new JumpParticleEntrySerializer());

            registry.register(new JumpSoundSerializer());
            registry.register(new JumpSoundEntrySerializer());
            registry.register(new SoundSerializer());
        };
    }

    @Override
    public @NotNull String getFileName() {
        return "visualConfig.yml";
    }
}
