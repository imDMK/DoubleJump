package com.github.imdmk.doublejump.feature.jump.fall;

import com.github.imdmk.doublejump.injector.PluginListener;
import org.bukkit.Location;
import org.bukkit.Material;
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
        if (this.jumpConfiguration.applyFallDamage) {
            return;
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.FALL
                || !(event.getEntity() instanceof Player player)) {
            return;
        }

        if (this.jumpCache.isActive(player.getUniqueId())) {
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
        if (!this.jumpConfiguration.applyFallDamage) {
            return;
        }

        Player player = event.getPlayer();
        if (player.isFlying() || !this.jumpCache.isActive(player.getUniqueId())) {
            return;
        }

        if (this.shouldReenableFallDamage(player)) {
            this.flyingService.disable(player, false);
        }
    }

    /**
     * Checks whether the player should have fall damage re-enabled.
     * The player must have fallen at least the minimum required distance and be standing on a non-air block.
     *
     * @param player The player to check.
     * @return true, if fall damage should be re-enabled.
     */
    private boolean shouldReenableFallDamage(@NotNull Player player) {
        return this.hasFallenEnough(player) && this.isStandingOnSolidBlock(player);
    }

    /**
     * Checks if the player has fallen at least the minimum distance to receive fall damage.
     *
     * @param player The player to check.
     * @return true, if fall distance is greater than or equal to a minimum threshold.
     */
    private boolean hasFallenEnough(@NotNull Player player) {
        return player.getFallDistance() >= MIN_FALL_DISTANCE_FOR_DAMAGE;
    }

    /**
     * Checks if the block directly beneath the player is solid (not air).
     *
     * @param player The player to check.
     * @return true, if the block below the player is not air.
     */
    private boolean isStandingOnSolidBlock(@NotNull Player player) {
        Location below = player.getLocation().clone().subtract(0, 1, 0);
        return below.getBlock().getType() != Material.AIR;
    }

}
