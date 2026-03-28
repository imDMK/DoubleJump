package com.github.imdmk.doublejump.core.platform.hook.worldguard;

import org.bukkit.entity.Player;

import java.util.Set;

@FunctionalInterface
public interface RegionProvider {

    Set<String> queryPlayerRegions(Player player);

    static RegionProvider empty() {
        return player -> Set.of();
    }

}
