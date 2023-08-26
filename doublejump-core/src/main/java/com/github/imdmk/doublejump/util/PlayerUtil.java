package com.github.imdmk.doublejump.util;

import com.github.imdmk.doublejump.jump.particle.JumpParticle;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public final class PlayerUtil {

    private PlayerUtil() {
        throw new UnsupportedOperationException("This is utility class.");
    }

    public static void playSound(Player player, Sound sound, float volume, float pitch) {
        if (sound == null) {
            return;
        }

        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public static void spawnParticle(Player player, JumpParticle jumpParticle, int count, double offsetX, double offsetY, double offsetZ, int extra) {
        Particle particle = jumpParticle.getType();

        if (particle.getDataType() == Void.class) { //Check if particle needs data
            player.spawnParticle(particle, player.getLocation(), count);
        }
        else {
            player.spawnParticle(
                    particle,
                    player.getLocation(),
                    count,
                    offsetX, offsetY, offsetZ, extra,
                    new Particle.DustOptions(jumpParticle.getColor(), jumpParticle.getSize())
            );
        }
    }

    public static void spawnParticles(Player player, List<JumpParticle> jumpParticles, int count, double offsetX, double offsetY, double offsetZ, int extra) {
        for (JumpParticle jumpParticle : jumpParticles) {
            spawnParticle(player, jumpParticle, count, offsetX, offsetY, offsetZ, extra);
        }
    }
}
