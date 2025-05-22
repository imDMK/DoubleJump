package com.github.imdmk.doublejump.feature.jump.particle;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a healing particle effect with customizable parameters.
 */
public final class JumpParticle {

    private final Particle particle;
    private final int count;
    private final Color color;
    private final int size;
    private final double offsetX;
    private final double offsetY;
    private final double offsetZ;
    private final double extra;

    private JumpParticle(
            @NotNull Particle particle,
            int count,
            @NotNull Color color,
            int size,
            double offsetX,
            double offsetY,
            double offsetZ,
            double extra
    ) {
        this.particle = particle;
        this.count = count;
        this.color = color;
        this.size = size;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.extra = extra;
    }

    public @NotNull Particle particle() {
        return this.particle;
    }

    public int count() {
        return this.count;
    }

    public @NotNull Color color() {
        return this.color;
    }

    public int size() {
        return this.size;
    }

    public double offsetX() {
        return this.offsetX;
    }

    public double offsetY() {
        return this.offsetY;
    }

    public double offsetZ() {
        return this.offsetZ;
    }

    public double extra() {
        return this.extra;
    }

    /**
     * Spawns the particle in the given world at the specified location.
     */
    public void spawn(@NotNull World world, @NotNull Location location) {
        world.spawnParticle(
                this.particle,
                location,
                this.count,
                this.offsetX, this.offsetY, this.offsetZ,
                this.extra
        );
    }

    /**
     * Returns a new builder instance for HealingParticle.
     */
    public static @NotNull Builder builder(@NotNull Particle particle, int count) {
        return new Builder(particle, count);
    }

    /**
     * Builder for creating immutable HealingParticle instances.
     */
    public static final class Builder {

        private final Particle particle;
        private final int count;
        private Color color = Color.WHITE;
        private int size = 0;
        private double offsetX = 0;
        private double offsetY = 0;
        private double offsetZ = 0;
        private double extra = 0;

        public Builder(@NotNull Particle particle, int count) {
            this.particle = particle;
            this.count = count;
        }

        @Contract("_-> this")
        public Builder color(@NotNull Color color) {
            this.color = color;
            return this;
        }

        @Contract("_-> this")
        public Builder size(int size) {
            this.size = size;
            return this;
        }

        @Contract("_-> this")
        public Builder offsetX(double offsetX) {
            this.offsetX = offsetX;
            return this;
        }

        @Contract("_-> this")
        public Builder offsetY(double offsetY) {
            this.offsetY = offsetY;
            return this;
        }

        @Contract("_-> this")
        public Builder offsetZ(double offsetZ) {
            this.offsetZ = offsetZ;
            return this;
        }

        @Contract("_-> this")
        public Builder extra(double extra) {
            this.extra = extra;
            return this;
        }

        public @NotNull JumpParticle build() {
            return new JumpParticle(this.particle, this.count, this.color, this.size, this.offsetX, this.offsetY, this.offsetZ, this.extra);
        }
    }
}
