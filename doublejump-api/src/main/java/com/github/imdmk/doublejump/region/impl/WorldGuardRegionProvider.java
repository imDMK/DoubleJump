package com.github.imdmk.doublejump.region.impl;

import com.github.imdmk.doublejump.region.RegionProvider;
import com.github.imdmk.doublejump.restriction.JumpRestriction;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.stream.Collectors;

public class WorldGuardRegionProvider implements RegionProvider {

    private final JumpRestriction regionRestriction;

    public WorldGuardRegionProvider(JumpRestriction regionRestriction) {
        this.regionRestriction = regionRestriction;
    }

    @Override
    public boolean isInAllowedRegion(Player player) {
        Set<String> playerRegions = this.getPlayerRegions(player).getRegions().stream()
                .map(ProtectedRegion::getId)
                .collect(Collectors.toSet());

        return this.regionRestriction.isAllowed(playerRegions);
    }

    public ApplicableRegionSet getPlayerRegions(Player player) {
        Location playerLocation = BukkitAdapter.adapt(player.getLocation());

        WorldGuardPlatform worldGuardPlatform = WorldGuard.getInstance().getPlatform();

        RegionContainer regionContainer = worldGuardPlatform.getRegionContainer();
        RegionQuery regionQuery = regionContainer.createQuery();

        return regionQuery.getApplicableRegions(playerLocation);
    }
}
