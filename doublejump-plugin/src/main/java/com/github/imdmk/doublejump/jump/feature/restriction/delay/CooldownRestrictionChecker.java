package com.github.imdmk.doublejump.jump.feature.restriction.delay;

import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.cache.JumpPlayerCache;
import com.github.imdmk.doublejump.jump.feature.restriction.RestrictionChecker;
import com.github.imdmk.doublejump.jump.feature.restriction.RestrictionDenyReason;
import com.github.imdmk.doublejump.jump.feature.restriction.RestrictionResult;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Objects;

public class CooldownRestrictionChecker implements RestrictionChecker {

    private final JumpPlayerCache jumpCache;

    public CooldownRestrictionChecker(@NotNull JumpPlayerCache jumpCache) {
        this.jumpCache = Objects.requireNonNull(jumpCache, "jumpCache cannot be null");
    }

    @Override
    public @NotNull RestrictionResult check(@NotNull Player player) {
        return this.jumpCache.getActive(player.getUniqueId())
                .filter(this::hasCooldown)
                .map(jumpPlayer -> RestrictionResult.failed(RestrictionDenyReason.JUMP_DELAY))
                .orElse(RestrictionResult.passed());
    }

    /**
     * Returns true if player’s last jump was within delay period.
     *
     * @param player jump player data, non-null
     * @return true if delay active, false otherwise
     */
    private boolean hasCooldown(@NotNull JumpPlayer player) {
        Instant now = Instant.now();
        return player.getNextAllowedJump()
                .map(now::isBefore)
                .orElse(false);
    }
}
