package com.github.imdmk.doublejump.jump.controller;

import com.github.imdmk.doublejump.infrastructure.injector.PluginListener;
import com.github.imdmk.doublejump.jump.event.DoubleJumpEvent;
import com.github.imdmk.doublejump.jump.feature.velocity.JumpVelocity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

/**
 * Controller responsible for handling the double jump mechanic,
 * including detecting flight toggle and executing the double jump action.
 */
public class DoubleJumpController extends PluginListener {

    /**
     * Executes the double jump when the DoubleJumpEvent is triggered.
     * Applies velocity to the player and disables flight.
     *
     * @param event Custom DoubleJumpEvent.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    void onDoubleJump(DoubleJumpEvent event) {
        Player player = event.getPlayer();
        JumpVelocity velocity = event.getJumpVelocity();

        Vector vector = player.getLocation().getDirection()
                .multiply(velocity.horizontalBoost())
                .setY(velocity.verticalBoost());

        this.flyingService.disable(player, false);
        player.setVelocity(vector);
    }

    /**
     * Handles the player toggling flight event to trigger double jump.
     * Cancels the default flight toggle and calls a custom DoubleJumpEvent.
     *
     * @param event PlayerToggleFlightEvent from Bukkit API.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    void onToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();

        this.jumpCache.getActive(player.getUniqueId())
                .ifPresent(jump -> {
                    event.setCancelled(true);
                    this.eventCaller.callEvent(new DoubleJumpEvent(player, jump));
                });
    }
}
