package me.dmk.doublejump.region.impl;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.dmk.doublejump.region.RegionProvider;
import org.bukkit.entity.Player;

import java.util.List;

public class WorldGuardRegionProvider implements RegionProvider {

    private final List<String> disabledRegions;

    public WorldGuardRegionProvider(List<String> disabledRegions) {
        this.disabledRegions = disabledRegions;
    }

    @Override
    public boolean isInRegion(Player player) {
        return this.disabledRegions.stream()
                .anyMatch(region -> this.isInRegion(player, region));
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
