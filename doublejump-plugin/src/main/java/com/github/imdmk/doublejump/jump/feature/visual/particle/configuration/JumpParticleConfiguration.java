package com.github.imdmk.doublejump.jump.feature.visual.particle.configuration;

import com.github.imdmk.doublejump.jump.feature.visual.particle.JumpParticle;
import com.github.imdmk.doublejump.jump.feature.visual.particle.gui.JumpParticleEntry;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import org.bukkit.Material;
import org.bukkit.Particle;

import java.util.List;

@Header({
        "# ",
        "# DoubleJump Premium - Particle Effects Configuration",
        "# Configure particle effects shown when a player performs a double jump.",
        "# ",
        "# 'enabled' toggles all double jump particle effects globally.",
        "# 'supportedParticles' lists all particle effects players can choose via GUI.",
        "# Each particle is linked with a corresponding item for the GUI selection.",
        "# ",
        "# Enjoying the plugin? Please leave a review on SpigotMC!",
        "# Support development: https://github.com/sponsors/imDMK",
        "# "
})
public class JumpParticleConfiguration extends OkaeriConfig {

    @Comment("# Enables or disables the double jump particle effect globally.")
    public boolean enabled = true;

    @Comment("# List of particles available for players to select in the GUI.")
    public List<JumpParticleEntry> supportedParticles = List.of(
            new JumpParticleEntry(new JumpParticle(Particle.HEART, 10), Material.RED_DYE),
            new JumpParticleEntry(new JumpParticle(Particle.FLAME, 100), Material.BLAZE_POWDER),
            new JumpParticleEntry(new JumpParticle(Particle.CRIT, 80), Material.DIAMOND_SWORD),
            new JumpParticleEntry(new JumpParticle(Particle.NOTE, 80), Material.NOTE_BLOCK),
            new JumpParticleEntry(new JumpParticle(Particle.PORTAL, 100), Material.ENDER_PEARL),
            new JumpParticleEntry(new JumpParticle(Particle.FLASH, 80), Material.GLOWSTONE),
            new JumpParticleEntry(new JumpParticle(Particle.LAVA, 80), Material.LAVA_BUCKET),
            new JumpParticleEntry(new JumpParticle(Particle.COMPOSTER, 50), Material.COMPOSTER),
            new JumpParticleEntry(new JumpParticle(Particle.DRAGON_BREATH, 100), Material.DRAGON_BREATH),
            new JumpParticleEntry(new JumpParticle(Particle.END_ROD, 100), Material.END_ROD),
            new JumpParticleEntry(new JumpParticle(Particle.DAMAGE_INDICATOR, 0), Material.DIAMOND_SWORD),
            new JumpParticleEntry(new JumpParticle(Particle.SWEEP_ATTACK, 50), Material.IRON_SWORD),
            new JumpParticleEntry(new JumpParticle(Particle.FALLING_DUST, 100), Material.SAND),
            new JumpParticleEntry(new JumpParticle(Particle.ELECTRIC_SPARK, 80), Material.LEVER),
            new JumpParticleEntry(new JumpParticle(Particle.GLOW, 80), Material.GLOWSTONE),
            new JumpParticleEntry(new JumpParticle(Particle.DUST_COLOR_TRANSITION, 80), Material.PAINTING),
            new JumpParticleEntry(new JumpParticle(Particle.DRIPPING_DRIPSTONE_WATER, 50), Material.WATER_BUCKET),
            new JumpParticleEntry(new JumpParticle(Particle.DRIPPING_DRIPSTONE_LAVA, 50), Material.LAVA_BUCKET),
            new JumpParticleEntry(new JumpParticle(Particle.FALLING_DRIPSTONE_WATER, 50), Material.WATER_BUCKET),
            new JumpParticleEntry(new JumpParticle(Particle.FALLING_DRIPSTONE_LAVA, 50), Material.LAVA_BUCKET),
            new JumpParticleEntry(new JumpParticle(Particle.FALLING_SPORE_BLOSSOM, 50), Material.SPORE_BLOSSOM),
            new JumpParticleEntry(new JumpParticle(Particle.SPORE_BLOSSOM_AIR, 50), Material.SPORE_BLOSSOM)
    );
}
