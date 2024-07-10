package com.github.imdmk.doublejump.jump.particle;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.Color;
import org.bukkit.Particle;

import java.util.List;

public class JumpParticleSettings extends OkaeriConfig {

    @Comment("# Specifies whether particles during a double jump are to be enabled")
    public boolean enabled = true;

    @Comment({
            "# List of particles during a double jump",
            "# Available particles: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Particle.html",
            "# Available colors: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Color.html"
    })
    public List<JumpParticle> particles = List.of(
            new JumpParticle(Particle.NOTE, Color.WHITE, 20, 3, 2, 0, 0, 0),
            new JumpParticle(Particle.BUBBLE, Color.WHITE, 40, 3, 2, 0, 0, 0)
    );

}
