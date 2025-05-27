package com.github.imdmk.doublejump.jump.feature.visual.particle;

import com.github.imdmk.doublejump.infrastructure.injector.PluginListener;
import com.github.imdmk.doublejump.jump.event.DoubleJumpEvent;
import com.github.imdmk.doublejump.jump.feature.visual.JumpVisual;
import com.github.imdmk.doublejump.jump.feature.visual.configuration.JumpVisualConfiguration;
import com.github.imdmk.doublejump.jump.feature.visual.repository.JumpVisualCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;

public class JumpParticleController extends PluginListener {

    @Inject private JumpVisualCache visualCache;
    @Inject private JumpVisualConfiguration visualConfiguration;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    void onDoubleJump(final DoubleJumpEvent event) {
        if (!this.visualConfiguration.particles.enabled) {
            return;
        }

        Player player = event.getPlayer();

        this.visualCache.getByUuid(player.getUniqueId())
                .ifPresent(settings -> this.spawnParticles(player, settings));
    }

    private void spawnParticles(@NotNull Player player, @NotNull JumpVisual visual) {
        visual.getJumpParticles().forEach(particle -> particle.spawn(player.getWorld(), player.getLocation()));
    }
}
