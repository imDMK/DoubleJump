package com.github.imdmk.doublejump.core.platform.hook.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.stream.Collectors;

final class WorldGuardRegionProvider
        implements RegionProvider {

    private static final RegionContainer REGION_CONTAINER = WorldGuard.getInstance()
            .getPlatform().getRegionContainer();

    @Override
    public Set<String> queryPlayerRegions(Player player) {
        Location location = BukkitAdapter.adapt(player.getLocation());
        ApplicableRegionSet regionSet = REGION_CONTAINER.createQuery()
                .getApplicableRegions(location);

        return regionSet.getRegions().stream()
                .map(ProtectedRegion::getId)
                .collect(Collectors.toUnmodifiableSet());
    }
}
