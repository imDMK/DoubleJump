package me.dmk.doublejump.util;

import me.dmk.doublejump.particle.JumpParticle;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class PlayerUtil {

    private PlayerUtil() {
        throw new UnsupportedOperationException("This is utility class.");
    }

    public static void playSound(Player player, Sound sound, float volume, float pitch) {
        if (sound == null) {
            return;
        }

        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public static void spawnParticles(Player player, List<JumpParticle> jumpParticles, int count, double offsetX, double offsetY, double offsetZ, int extra) {
        for (JumpParticle jumpParticle : jumpParticles) {
            Particle particle = jumpParticle.getType();
            Class<?> dataType = particle.getDataType();

            if (dataType == Void.class) { //Check if particle needs data
                player.spawnParticle(
                        particle,
                        player.getLocation(),
                        count
                );
            } else {
                player.spawnParticle(
                        particle,
                        player.getLocation(),
                        count,
                        offsetX, offsetY, offsetZ, extra,
                        new Particle.DustOptions(getColorFromName(jumpParticle.getColor()), jumpParticle.getSize())
                );
            }
        }
    }

    public static Color getColorFromName(String colorName) {
        return Arrays.stream(Color.class.getFields())
                .filter(field -> field.getType().equals(Color.class))
                .filter(field -> field.getName().equalsIgnoreCase(colorName))
                .map(field -> {
                    try {
                        return (Color) field.get(Color.class);
                    } catch (IllegalAccessException exception) {
                        throw new RuntimeException(exception);
                    }
                })
                .findFirst()
                .orElse(Color.WHITE);
    }
}
