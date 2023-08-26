package me.dmk.doublejump.region.impl;

import me.dmk.doublejump.region.RegionProvider;
import org.bukkit.entity.Player;

public class EmptyRegionProvider implements RegionProvider {

    @Override
    public boolean isInRegion(Player player) {
        return false;
    }
}
