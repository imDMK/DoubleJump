package com.github.imdmk.doublejump.feature.jump.sound;

import com.github.imdmk.doublejump.injector.PluginListener;
import com.github.imdmk.doublejump.jump.DoubleJumpEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class JumpSoundController extends PluginListener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    void onDoubleJump(final DoubleJumpEvent event) {
        if (!this.jumpConfiguration.sounds.enabled) {
            return;
        }

        Player player = event.getPlayer();
        for (JumpSound jumpSound : this.jumpConfiguration.sounds.jump) {
            player.playSound(player, jumpSound.sound(), jumpSound.volume(), jumpSound.pitch());
        }
    }
}
