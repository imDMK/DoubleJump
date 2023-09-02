package com.github.imdmk.doublejump.region;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface RegionProvider {

    /**
     *
     * @return true if the player is in disabled area
     */
    boolean isInRegion(Player player);
}
