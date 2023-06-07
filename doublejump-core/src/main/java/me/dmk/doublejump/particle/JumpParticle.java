package me.dmk.doublejump.particle;

import eu.okaeri.configs.OkaeriConfig;
import org.bukkit.Particle;

public class JumpParticle extends OkaeriConfig {

    private Particle type;
    private String color;
    private int size;

    public JumpParticle(Particle particle, String color, int size) {
        this.type = particle;
        this.color = color;
        this.size = size;
    }

    public Particle getType() {
        return this.type;
    }

    public void setType(Particle particle) {
        this.type = particle;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
