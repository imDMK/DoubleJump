package com.github.imdmk.doublejump.feature.jump.fall;

import com.github.imdmk.doublejump.injector.PluginListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

/**
 * This controller handles two related responsibilities regarding fall damage and flight state.
 */
public class JumpFallDamageController extends PluginListener {

    /**
     * The minimum fall distance (in blocks) a player must fall
     * before taking fall damage, matching Minecraft's vanilla threshold.
     */
    private static final float MIN_FALL_DISTANCE_FOR_DAMAGE = 4.0F;

    /**
     * Cancels fall damage for players in the jump cache if fall damage is disabled in the configuration.
     * This method listens to damage events and prevents fall damage by cancelling the event and setting damage to zero.
     *
     * @param event The EntityDamageEvent to handle.
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    void onPlayerDamage(final EntityDamageEvent event) {
        if (!this.jumpConfiguration.fallDamage) {
            return;
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.FALL
                || !(event.getEntity() instanceof Player player)) {
            return;
        }

        if (this.jumpCache.hasPlayer(player.getUniqueId())) {
            event.setCancelled(true);
            event.setDamage(0);
        }
    }


    /**
     * Re-enables fall damage by disabling flight allowance for players who have landed,
     * assuming fall damage is enabled in the configuration.
     * This method listens to player movement to detect when players should take fall damage again.
     *
     * @param event The PlayerMoveEvent to handle.
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    void onPlayerMove(final PlayerMoveEvent event) {
        if (!this.jumpConfiguration.fallDamage) {
            return;
        }

        Player player = event.getPlayer();
        if (player.isFlying() || !this.jumpCache.hasPlayer(player.getUniqueId())) {
            return;
        }

        if (this.shouldReenableFallDamage(player)) {
            player.setAllowFlight(false);
        }
    }

    /**
     * Determines whether the player should be allowed to take fall damage again.
     * This is typically called after a double jump to re-enable normal fall physics.
     *
     * @param player The player to check.
     * @return true, if conditions suggest the player has landed and should take fall damage.
     */
    private boolean shouldReenableFallDamage(@NotNull Player player) {
        return this.hasFallenEnough(player) && this.isStandingOnSolid(player) && this.isLandingPredicted(player);
    }

    /**
     * Checks if the player has fallen a sufficient distance to incur fall damage.
     *
     * @param player The player whose fall distance is being evaluated.
     * @return true if the player's fall distance is equal to or greater than the configured minimum distance required to cause fall damage; false otherwise.
     */
    private boolean hasFallenEnough(@NotNull Player player) {
        return player.getFallDistance() >= MIN_FALL_DISTANCE_FOR_DAMAGE;
    }

    /**
     * Checks if the player is currently standing on a solid block.
     *
     * @param player The player to check.
     * @return true if there is a non-air block directly beneath the player.
     */
    private boolean isStandingOnSolid(@NotNull Player player) {
        Location locationBelow = player.getLocation().clone().subtract(0, 1, 0);
        return locationBelow.getBlock().getType().isSolid();
    }

    /**
     * Predicts whether the player is about to land based on their current velocity.
     *
     * @param player The player to evaluate.
     * @return true if the block in the direction of movement is not air.
     */
    private boolean isLandingPredicted(@NotNull Player player) {
        Location projectedLocation = player.getLocation().clone().add(player.getVelocity());
        return projectedLocation.getBlock().getType().isSolid();
    }
}
