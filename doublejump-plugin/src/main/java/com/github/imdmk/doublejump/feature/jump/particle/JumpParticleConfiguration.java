package com.github.imdmk.doublejump.feature.jump.particle;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.Particle;

import java.util.List;

public class JumpParticleConfiguration extends OkaeriConfig {

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
