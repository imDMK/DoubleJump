package com.github.imdmk.doublejump.feature.jump.restriction;

import com.github.imdmk.doublejump.feature.jump.JumpConfiguration;
import com.github.imdmk.doublejump.feature.jump.restriction.checker.RestrictionChecker;
import com.github.imdmk.doublejump.feature.jump.restriction.checker.impl.PermissionRestrictionChecker;
import com.github.imdmk.doublejump.feature.jump.restriction.checker.impl.PlayerPingRestrictionChecker;
import com.github.imdmk.doublejump.feature.jump.restriction.checker.impl.SetRestrictionChecker;
import com.github.imdmk.doublejump.feature.jump.restriction.checker.result.RestrictionDenyReason;
import com.github.imdmk.doublejump.feature.jump.restriction.checker.result.RestrictionResult;
import com.github.imdmk.doublejump.feature.jump.restriction.delay.DelayRestrictionChecker;
import com.github.imdmk.doublejump.jump.JumpPlayerCache;
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
public class JumpRestrictionService {

    @Inject private JumpConfiguration jumpConfiguration;
    @Inject private JumpPlayerCache jumpCache;

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
                new DelayRestrictionChecker(this.jumpConfiguration, this.jumpCache),
                new PlayerPingRestrictionChecker(configuration.disableIfPlayerLagging),
                new SetRestrictionChecker<>(
                        configuration.disabledWorlds,
                        p -> p.getWorld().getName(),
                        RestrictionDenyReason.WORLD_DISABLED
                ),
                new SetRestrictionChecker<>(
                        configuration.disabledGameModes,
                        Player::getGameMode,
                        RestrictionDenyReason.GAME_MODE_BLOCKED
                ),
                new PermissionRestrictionChecker(
                        configuration.allowedPermissions
                )
        );
    }
}
