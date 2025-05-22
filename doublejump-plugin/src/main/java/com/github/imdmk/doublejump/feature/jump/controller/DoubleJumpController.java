package com.github.imdmk.doublejump.feature.jump.controller;

import com.github.imdmk.doublejump.injector.PluginListener;
import com.github.imdmk.doublejump.jump.DoubleJumpEvent;
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
    void onDoubleJump(final DoubleJumpEvent event) {
        final Player player = event.getPlayer();

        final Vector vector = player.getLocation().getDirection()
                .multiply(this.jumpConfiguration.horizontalBoost)
                .setY(this.jumpConfiguration.verticalBoost);

        this.flyingService.disable(player, false);
        player.setVelocity(vector);
    }

    /**
     * Handles the player toggling flight event to trigger double jump.
     * Cancels the default flight toggle and calls a custom DoubleJumpEvent.
     *
     * @param event PlayerToggleFlightEvent from Bukkit API.
     */
    @EventHandler(ignoreCancelled = true)
    void onToggleFlight(final PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();

        this.jumpCache.getActive(player.getUniqueId())
                .ifPresent(jumpPlayer -> {
                    event.setCancelled(true);
                    this.server.getPluginManager().callEvent(new DoubleJumpEvent(player, jumpPlayer));
                });
    }
}
