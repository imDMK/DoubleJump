package me.dmk.doublejump.util;

import me.dmk.doublejump.particle.JumpParticle;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class PlayerUtil {

    private PlayerUtil() {
        throw new UnsupportedOperationException("This is utility class.");
    }

    private static final Map<String, Color> colors = new ConcurrentHashMap<>();

    static {
        for (Field field : Color.class.getFields()) {
            if (!field.getType().equals(Color.class)) {
                continue;
            }

            try {
                colors.put(field.getName(), (Color) field.get(Color.class));
            }
            catch (IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }
        }
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

            if (particle.getDataType() == Void.class) { //Check if particle needs data
                player.spawnParticle(particle, player.getLocation(), count);
            }
            else {
                player.spawnParticle(
                        particle,
                        player.getLocation(),
                        count,
                        offsetX, offsetY, offsetZ, extra,
                        new Particle.DustOptions(
                                getColorFromName(jumpParticle.getColor()),
                                jumpParticle.getSize()
                        )
                );
            }
        }
    }

    public static Color getColorFromName(String colorName) {
        return Optional.ofNullable(colors.get(colorName))
                .orElseThrow(() -> new IllegalStateException("Invalid color name: " + colorName));
    }
}
