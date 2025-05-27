package com.github.imdmk.doublejump.jump.feature.visual.particle;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a particle effect with customizable parameters.
 */
public record JumpParticle(@NotNull Particle particle, int count) {

    /**
     * Gets the name of particle
     */
    public @NotNull String particleName() {
        return this.particle.name();
    }

    /**
     * Spawns the particle in the given world at the specified location.
     */
    public void spawn(@NotNull World world, @NotNull Location location) {
        world.spawnParticle(this.particle, location, this.count);
    }
}
