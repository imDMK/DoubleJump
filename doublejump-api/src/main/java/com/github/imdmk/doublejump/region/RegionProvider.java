package com.github.imdmk.doublejump.region;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface RegionProvider {

    /**
     * Checks whether the player is within an area covered by the region restriction.
     *
     * @param player The player whose location is being checked.
     * @return true if the player is within an area covered by the restriction, otherwise false.
     */
    boolean isInAllowedRegion(Player player);
}
