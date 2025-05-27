package com.github.imdmk.doublejump.jump.feature.visual.particle.gui;

import com.github.imdmk.doublejump.jump.feature.visual.particle.JumpParticle;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a mapping between a Minecraft particle effect and its corresponding GUI display item.
 *
 * @param particle   The particle effect.
 * @param displayItem The item used to visually represent the particle in the GUI.
 */
public record JumpParticleEntry(@NotNull JumpParticle particle, @NotNull Material displayItem) {
}
