package me.dmk.doublejump.region;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface RegionProvider {

    boolean isInRegion(Player player);
}
