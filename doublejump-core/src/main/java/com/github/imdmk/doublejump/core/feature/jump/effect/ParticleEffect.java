package com.github.imdmk.doublejump.core.feature.jump.effect;

import org.bukkit.Particle;
import org.bukkit.entity.Player;

public record ParticleEffect(Particle particle, int count, double offset, double extra)
        implements JumpEffect {

    @Override
    public void apply(Player player) {
        player.spawnParticle(
                particle,
                player.getLocation(),
                count,
                offset, offset, offset,
                extra
        );
    }

    @Override
    public JumpEffectType type() {
        return JumpEffectType.PARTICLE;
    }
}
