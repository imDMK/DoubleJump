package com.github.imdmk.doublejump.feature.jump.particle;

import com.github.imdmk.doublejump.injector.PluginListener;
import com.github.imdmk.doublejump.jump.DoubleJumpEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class JumpParticleController extends PluginListener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    void onDoubleJump(final DoubleJumpEvent event) {
        if (!this.jumpConfiguration.particles.enabled) {
            return;
        }

        Player player = event.getPlayer();
        this.jumpConfiguration.particles.jump.spawn(player.getWorld(), player.getLocation());
    }
}
