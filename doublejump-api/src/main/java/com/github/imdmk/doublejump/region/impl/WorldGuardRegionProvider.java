package com.github.imdmk.doublejump.region.impl;

import com.github.imdmk.doublejump.region.RegionProvider;
import com.github.imdmk.doublejump.restriction.JumpRestriction;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.entity.Player;

public class WorldGuardRegionProvider implements RegionProvider {

    private final JumpRestriction regionRestriction;

    public WorldGuardRegionProvider(JumpRestriction regionRestriction) {
        this.regionRestriction = regionRestriction;
    }

    @Override
    public boolean isInAllowedRegion(Player player) {
        return switch (this.regionRestriction.type()) {
            case BLACKLIST -> this.regionRestriction.list().stream().anyMatch(region -> !this.isInRegion(player, region));
            case WHITELIST -> this.regionRestriction.list().stream().anyMatch(region -> this.isInRegion(player, region));
        };
    }

    public boolean isInRegion(Player player, String regionId) {
        Location playerLocation = BukkitAdapter.adapt(player.getLocation());

        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery regionQuery = regionContainer.createQuery();
        ApplicableRegionSet applicableRegionSet = regionQuery.getApplicableRegions(playerLocation);

        return applicableRegionSet.getRegions().stream()
                .anyMatch(region -> region.getId().equals(regionId));
    }
}
