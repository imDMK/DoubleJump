package com.github.imdmk.doublejump.jump.restriction;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Functional interface representing a single restriction check for a player.
 * <p>
 * Implementations of this interface encapsulate the logic to determine whether
 * a player passes or fails a specific restriction, such as permission, world,
 * or game mode restrictions.
 * </p>
 */
@FunctionalInterface
public interface RestrictionChecker {

    /**
     * Checks if the given player satisfies the restriction.
     *
     * @param player the player to check, must not be null
     * @return a {@link RestrictionResult} indicating whether the restriction
     *         was passed or failed, never null
     */
    @NotNull RestrictionResult check(@NotNull Player player);
}
