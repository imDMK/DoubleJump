package com.github.imdmk.doublejump.feature.jump.restriction.delay;

import com.github.imdmk.doublejump.feature.jump.JumpConfiguration;
import com.github.imdmk.doublejump.feature.jump.restriction.checker.RestrictionChecker;
import com.github.imdmk.doublejump.feature.jump.restriction.checker.result.RestrictionDenyReason;
import com.github.imdmk.doublejump.feature.jump.restriction.checker.result.RestrictionResult;
import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.JumpPlayerCache;
import com.github.imdmk.doublejump.util.DurationUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class DelayRestrictionChecker implements RestrictionChecker {

    private final JumpConfiguration jumpConfiguration;
    private final JumpPlayerCache jumpCache;

    public DelayRestrictionChecker(
            @NotNull JumpConfiguration jumpConfiguration,
            @NotNull JumpPlayerCache jumpCache
    ) {
        this.jumpConfiguration = Objects.requireNonNull(jumpConfiguration, "jumpConfiguration cannot be null");
        this.jumpCache = Objects.requireNonNull(jumpCache, "jumpCache cannot be null");
    }

    @Override
    public @NotNull RestrictionResult check(@NotNull Player player) {
        return this.jumpCache.getActive(player.getUniqueId())
                .filter(this::hasJumpDelay)
                .map(jumpPlayer -> RestrictionResult.failed(RestrictionDenyReason.JUMP_DELAY))
                .orElse(RestrictionResult.passed());
    }

    /**
     * Returns true if player’s last jump was within delay period.
     *
     * @param player jump player data, non-null
     * @return true if delay active, false otherwise
     */
    private boolean hasJumpDelay(@NotNull JumpPlayer player) {
        Duration delay = this.jumpConfiguration.jumpDelay;
        if (!DurationUtil.isValid(delay)) {
            return false;
        }

        Instant now = Instant.now();

        return player.getLastJump()
                .map(lastJump -> now.isBefore(lastJump.plus(delay)))
                .orElse(false);
    }
}
