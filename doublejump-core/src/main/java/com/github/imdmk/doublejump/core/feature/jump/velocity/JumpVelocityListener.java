package com.github.imdmk.doublejump.core.feature.jump.velocity;

import com.github.imdmk.doublejump.core.feature.jump.JumpConfig;
import com.github.imdmk.doublejump.core.feature.jump.JumpEvent;
import com.github.imdmk.doublejump.core.injector.annotations.PluginListener;
import com.github.imdmk.doublejump.core.shared.permission.PermissionBasedValueProvider;
import com.github.imdmk.doublejump.core.shared.permission.PlayerValueProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.panda_lang.utilities.inject.annotations.Inject;

@PluginListener
final class JumpVelocityListener implements Listener {

    private final JumpVelocityService velocityService;
    private final PlayerValueProvider<JumpVelocity> velocityProvider;

    @Inject
    JumpVelocityListener(JumpConfig config, JumpVelocityService velocityService) {
        this.velocityService = velocityService;
        this.velocityProvider = new PermissionBasedValueProvider<>(config.velocities);
    }

    @EventHandler(ignoreCancelled = true)
    void onJump(JumpEvent event) {
        Player player = event.getPlayer();
        JumpVelocity velocity = velocityProvider.resolve(player);

        velocityService.applyVelocity(player, velocity);
    }
}
