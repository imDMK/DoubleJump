package com.github.imdmk.doublejump.jump.feature.item.usage;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a strategy for determining whether a double-jump item is considered "used" by a player.
 * <p>
 * Implementations of this interface define how the plugin checks if a player is actively using the jump item
 * — for example, by holding it, wearing it, or simply having it in the inventory.
 *
 * <p>Common implementations:
 * <ul>
 *   <li>{@code HoldItemUsageStrategy} – player must be holding the item.</li>
 *   <li>{@code WearItemUsageStrategy} – player must wear the item.</li>
 *   <li>{@code HaveItemUsageStrategy} – player must have the item in inventory.</li>
 * </ul>
 */
@FunctionalInterface
public interface ItemUsageStrategy {

    /**
     * Checks whether the given player is currently using the jump item according to this strategy.
     *
     * @param player the player to evaluate (never null)
     * @return {@code true} if the player satisfies the usage condition, {@code false} otherwise
     */
    boolean isItemUsed(@NotNull Player player);

}
