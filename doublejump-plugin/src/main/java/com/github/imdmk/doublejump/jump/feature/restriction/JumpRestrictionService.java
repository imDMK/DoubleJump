package com.github.imdmk.doublejump.jump.feature.restriction;

import com.github.imdmk.doublejump.jump.JumpConfig;
import com.github.imdmk.doublejump.jump.cache.JumpPlayerCache;
import com.github.imdmk.doublejump.jump.feature.restriction.checker.SetRestrictionChecker;
import com.github.imdmk.doublejump.jump.feature.restriction.checker.WorldGuardRestrictionChecker;
import com.github.imdmk.doublejump.jump.feature.restriction.checker.player.GlidingRestrictionChecker;
import com.github.imdmk.doublejump.jump.feature.restriction.checker.player.PermissionRestrictionChecker;
import com.github.imdmk.doublejump.jump.feature.restriction.checker.player.PingRestrictionChecker;
import com.github.imdmk.doublejump.jump.feature.restriction.delay.CooldownRestrictionChecker;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;
import org.panda_lang.utilities.inject.annotations.PostConstruct;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Service responsible for evaluating whether a player is restricted from using double jump.
 * <p>
 * It evaluates multiple restriction types (e.g., world, game mode, permissions) using registered checkers.
 * Each restriction returns a {@link RestrictionResult} that encapsulates success state and failure reason.
 * </p>
 */
public class JumpRestrictionService implements RestrictionChecker {

    @Inject private JumpConfig jumpConfig;
    @Inject private JumpPlayerCache jumpCache;

    /** List of active restriction checkers based on configuration. */
    private List<RestrictionChecker> checkers;

    @PostConstruct
    private void postConstruct() {
        this.checkers = this.createDefaultCheckers(this.jumpConfig.restrictions);
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
        return this.checkAllRestrictions(player).failure();
    }

    /**
     * Executes the provided action if the player is currently restricted from using double jump.
     * <p>
     * This is a utility method that internally runs all registered {@link RestrictionChecker}s via
     * {@link #checkAllRestrictions(Player)}.
     * If the result indicates failure, the specified
     * consumer is invoked with the corresponding {@link RestrictionResult}.
     * </p>
     *
     * @param player        the player to evaluate
     * @param onRestricted  the action to execute if the player is restricted
     */
    public void ifRestricted(@NotNull Player player, @NotNull Consumer<RestrictionResult> onRestricted) {
        Optional.of(this.checkAllRestrictions(player))
                .filter(RestrictionResult::failure)
                .ifPresent(onRestricted);
    }

    /**
     * Creates a list of default restriction checkers based on provided configuration.
     *
     * @param configuration the configuration containing restriction sets
     * @return list of initialized restriction checkers
     */
    private List<RestrictionChecker> createDefaultCheckers(@NotNull JumpRestrictionConfig configuration) {
        return List.of(
                new CooldownRestrictionChecker(this.jumpCache),
                new PingRestrictionChecker(configuration.disableIfPlayerLagging),
                new SetRestrictionChecker<>(
                        configuration.disabledWorlds,
                        p -> p.getWorld().getName(),
                        RestrictionDenyReason.WORLD_DISABLED
                ),
                new WorldGuardRestrictionChecker(configuration.disabledRegions),
                new SetRestrictionChecker<>(
                        configuration.disabledGameModes,
                        Player::getGameMode,
                        RestrictionDenyReason.GAME_MODE_BLOCKED
                ),
                new PermissionRestrictionChecker(configuration.allowedPermissions),
                new GlidingRestrictionChecker(configuration.blockUsageWhileGliding)
        );
    }

    @Override
    public @NotNull RestrictionResult check(@NotNull Player player) {
        return this.checkAllRestrictions(player);
    }
}
