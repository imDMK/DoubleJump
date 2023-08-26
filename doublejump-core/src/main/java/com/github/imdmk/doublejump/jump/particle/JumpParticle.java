package com.github.imdmk.doublejump.jump.particle;

import org.bukkit.Color;
import org.bukkit.Particle;

public class JumpParticle {

    private final Particle type;
    private final Color color;
    private final int size;

    public JumpParticle(Particle particle, Color color, int size) {
        this.type = particle;
        this.color = color;
        this.size = size;
    }

    public Particle getType() {
        return this.type;
    }

    public Color getColor() {
        return this.color;
    }

    public int getSize() {
        return this.size;
    }
}
