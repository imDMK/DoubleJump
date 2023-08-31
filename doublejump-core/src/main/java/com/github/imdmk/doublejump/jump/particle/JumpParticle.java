package com.github.imdmk.doublejump.jump.particle;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public record JumpParticle(Particle particle, Color color, int size, int count, double offsetX, double offsetY, double offsetZ,
                           double extra) {

    public void spawn(Player player) {
        if (this.particle.getDataType() == Void.class) { //Check if particle needs data
            player.spawnParticle(this.particle, player.getLocation(), this.count);
        }
        else {
            player.spawnParticle(
                    this.particle,
                    player.getLocation(),
                    this.count,
                    this.offsetX, this.offsetY, this.offsetZ, this.extra,
                    new Particle.DustOptions(this.color, this.size)
            );
        }
    }
}
