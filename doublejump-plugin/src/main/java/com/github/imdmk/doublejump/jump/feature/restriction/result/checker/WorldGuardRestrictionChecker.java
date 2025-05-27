package com.github.imdmk.doublejump.jump.feature.restriction.result.checker;

import com.github.imdmk.doublejump.jump.feature.restriction.RestrictionChecker;
import com.github.imdmk.doublejump.jump.feature.restriction.RestrictionDenyReason;
import com.github.imdmk.doublejump.jump.feature.restriction.RestrictionResult;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Restriction checker that blocks double jump for players in specified WorldGuard regions.
 */
public class WorldGuardRestrictionChecker implements RestrictionChecker {

    private final Set<String> disabledRegions;

    /**
     * Creates a new instance of the WorldGuard restriction checker.
     *
     * @param disabledRegions list of region IDs where double jump should be restricted
     */
    public WorldGuardRestrictionChecker(@NotNull Set<String> disabledRegions) {
        this.disabledRegions = Objects.requireNonNull(disabledRegions, "disabledRegions cannot be null");
    }

    @Override
    public @NotNull RestrictionResult check(@NotNull Player player) {
        if (this.disabledRegions.isEmpty()) {
            return RestrictionResult.passed();
        }

        Set<String> playerRegionIds = this.getPlayerRegions(player).getRegions().stream()
                .map(ProtectedRegion::getId)
                .collect(Collectors.toSet());

        for (String disabledRegion : this.disabledRegions) {
            if (playerRegionIds.contains(disabledRegion)) {
                return RestrictionResult.failed(RestrictionDenyReason.REGION_DISABLED);
            }
        }

        return RestrictionResult.passed();
    }

    /**
     * Retrieves the set of WorldGuard regions the player is currently in.
     *
     * @param player the player whose location will be evaluated
     * @return the applicable region set at the player's current location
     */
    public @NotNull ApplicableRegionSet getPlayerRegions(@NotNull Player player) {
        Location adaptedLocation = BukkitAdapter.adapt(player.getLocation());

        WorldGuardPlatform platform = WorldGuard.getInstance().getPlatform();
        RegionContainer container = platform.getRegionContainer();

        return container.createQuery().getApplicableRegions(adaptedLocation);
    }
}
