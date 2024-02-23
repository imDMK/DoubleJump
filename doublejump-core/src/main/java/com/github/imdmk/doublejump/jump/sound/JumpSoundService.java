package com.github.imdmk.doublejump.jump.sound;

import com.github.imdmk.doublejump.configuration.implementation.PluginConfiguration;
import com.github.imdmk.doublejump.jump.JumpSettings;
import com.github.imdmk.doublejump.placeholder.PlaceholderRegistry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class JumpSoundService {

    private final JumpSoundSettings jumpSoundSettings;

    public JumpSoundService(PluginConfiguration pluginConfiguration) {
        this.jumpSoundSettings = pluginConfiguration.jumpSettings.soundSettings;
    }

    public void play(Player player) {
        this.jumpSoundSettings.sounds.forEach(sound -> sound.play(player));
    }

    public void playNearby(Player player) {
        double nearbyX = this.jumpSoundSettings.playNearbyX;
        double nearbyY = this.jumpSoundSettings.playNearbyY;
        double nearbyZ = this.jumpSoundSettings.playNearbyZ;

        List<Entity> nearbyEntities = player.getNearbyEntities(nearbyX, nearbyY, nearbyZ);

        for (Entity entity : nearbyEntities) {
            if (!(entity instanceof Player other)) {
                return;
            }

            this.play(other);
        }
    }

    public boolean isSoundsEnabled() {
        return this.jumpSoundSettings.enabled;
    }

    public boolean isPlayNearbyEnabled() {
        return this.jumpSoundSettings.playNearbyEnabled;
    }
}
