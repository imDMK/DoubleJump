package me.dmk.doublejump.hook;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.entity.Player;

import java.util.List;

public class WorldGuardHook {

    private final boolean hooked;
    private final List<String> disabledRegions;

    public WorldGuardHook(boolean hooked, List<String> disabledRegions) {
        this.hooked = hooked;
        this.disabledRegions = disabledRegions;
    }

    public boolean isInRegion(Player player) {
        if (!this.hooked) {
            return false;
        }

        return this.disabledRegions.stream()
                .anyMatch(region -> this.isInRegion(player, region));
    }

    public boolean isInRegion(Player player, String regionId) {
        if (!this.hooked) {
            return false;
        }

        Location playerLocation = BukkitAdapter.adapt(player.getLocation());

        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery regionQuery = regionContainer.createQuery();
        ApplicableRegionSet applicableRegionSet = regionQuery.getApplicableRegions(playerLocation);

        return applicableRegionSet.getRegions().stream()
                .anyMatch(region -> region.getId().equals(regionId));
    }

    public boolean isHooked() {
        return this.hooked;
    }
}
