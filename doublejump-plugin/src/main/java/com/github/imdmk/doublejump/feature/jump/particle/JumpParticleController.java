package com.github.imdmk.doublejump.feature.jump.particle;

import com.github.imdmk.doublejump.injector.PluginListener;
import com.github.imdmk.doublejump.jump.event.DoubleJumpEvent;
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
        for (JumpParticle particle : this.jumpConfiguration.particles.jump) {
            particle.spawn(player.getWorld(), player.getLocation());
        }
    }
}
