package com.github.imdmk.doublejump.region.impl;

import com.github.imdmk.doublejump.region.RegionProvider;
import org.bukkit.entity.Player;

public class EmptyRegionProvider implements RegionProvider {

    @Override
    public boolean isInRegion(Player player) {
        return false;
    }
}
