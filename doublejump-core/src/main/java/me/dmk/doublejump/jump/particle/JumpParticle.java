package me.dmk.doublejump.jump.particle;

import org.bukkit.Particle;

public class JumpParticle {

    private final Particle type;
    private final String color;
    private final int size;

    public JumpParticle(Particle particle, String color, int size) {
        this.type = particle;
        this.color = color;
        this.size = size;
    }

    public Particle getType() {
        return this.type;
    }

    public String getColor() {
        return this.color;
    }

    public int getSize() {
        return this.size;
    }
}
