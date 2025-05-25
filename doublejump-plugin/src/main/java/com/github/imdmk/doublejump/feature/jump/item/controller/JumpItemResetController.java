package com.github.imdmk.doublejump.feature.jump.item.controller;

import com.github.imdmk.doublejump.injector.PluginListener;
import com.github.imdmk.doublejump.jump.JumpActivationType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

public class JumpItemResetController extends PluginListener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    void onPlayerDeath(final PlayerDeathEvent event) {
        Player player = event.getEntity();

        this.jumpCache.getActive(player.getUniqueId())
                .filter(jump -> jump.isActivationType(JumpActivationType.ITEM))
                .ifPresent(jump -> {
                    jump.setJumpAllowed(true);
                    jump.setActive(false);
                    jump.setActivationType(JumpActivationType.NONE);

                    this.flyingService.disable(player);
                });
    }
}
