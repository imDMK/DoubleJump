package com.github.imdmk.doublejump.region.impl;

import com.github.imdmk.doublejump.region.RegionProvider;
import org.bukkit.entity.Player;

public class EmptyRegionProvider implements RegionProvider {

    @Override
    public boolean isInAllowedRegion(Player player) {
        return false;
    }
}
