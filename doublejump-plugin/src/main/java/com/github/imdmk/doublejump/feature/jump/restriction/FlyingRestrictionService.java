package com.github.imdmk.doublejump.feature.jump.restriction;

import com.github.imdmk.doublejump.feature.jump.configuration.JumpConfiguration;
import com.github.imdmk.doublejump.feature.jump.restriction.result.RestrictionDenyReason;
import com.github.imdmk.doublejump.feature.jump.restriction.result.RestrictionResult;
import com.github.imdmk.doublejump.feature.jump.restriction.result.checker.PermissionRestrictionChecker;
import com.github.imdmk.doublejump.feature.jump.restriction.result.checker.RestrictionChecker;
import com.github.imdmk.doublejump.feature.jump.restriction.result.checker.SetRestrictionChecker;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;
import org.panda_lang.utilities.inject.annotations.PostConstruct;

import java.util.List;

/**
 * Service responsible for evaluating whether a player is restricted from using double jump.
 * <p>
 * It evaluates multiple restriction types (e.g., world, game mode, permissions) using registered checkers.
 * Each restriction returns a {@link RestrictionResult} that encapsulates success state and failure reason.
 * </p>
 */
public class FlyingRestrictionService {

    @Inject private JumpConfiguration jumpConfiguration;

    /** List of active restriction checkers based on configuration. */
    private List<RestrictionChecker> checkers;

    @PostConstruct
    private void postConstruct() {
        this.checkers = this.createDefaultCheckers(this.jumpConfiguration.restrictions);
    }

    /**
     * Runs all registered restriction checkers against the given player.
     *
     * @param player the player to evaluate
     * @return the result of the first failed restriction, or a passed result if all checks succeed
     */
    public @NotNull RestrictionResult checkAllRestrictions(@NotNull Player player) {
        for (RestrictionChecker checker : this.checkers) {
            RestrictionResult result = checker.check(player);
            if (!result.success()) {
                return result;
            }
        }

        return RestrictionResult.passed();
    }

    /**
     * Checks whether the player is restricted from using double jump.
     * Shortcut for evaluating {@link #checkAllRestrictions(Player)} and checking failure state.
     *
     * @param player the player to evaluate
     * @return {@code true} if any restriction applies; {@code false} otherwise
     */
    public boolean isRestricted(@NotNull Player player) {
        return !this.checkAllRestrictions(player).success();
    }

    /**
     * Creates a list of default restriction checkers based on provided configuration.
     *
     * @param configuration the configuration containing restriction sets
     * @return list of initialized restriction checkers
     */
    private List<RestrictionChecker> createDefaultCheckers(@NotNull JumpConfiguration.JumpRestrictionConfiguration configuration) {
        return List.of(
                new SetRestrictionChecker<>(
                        configuration.worldWhitelist,
                        configuration.worldBlacklist,
                        p -> p.getWorld().getName(),
                        RestrictionDenyReason.WORLD_DISABLED
                ),
                new SetRestrictionChecker<>(
                        configuration.gameModeWhitelist,
                        configuration.gameModeBlacklist,
                        Player::getGameMode,
                        RestrictionDenyReason.GAME_MODE_BLOCKED
                ),
                new PermissionRestrictionChecker(
                        configuration.permissionWhitelist,
                        configuration.permissionBlacklist
                )
        );
    }
}
