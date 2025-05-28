package com.github.imdmk.doublejump.jump.feature.restriction.checker.player;

import com.github.imdmk.doublejump.jump.feature.restriction.RestrictionChecker;
import com.github.imdmk.doublejump.jump.feature.restriction.RestrictionDenyReason;
import com.github.imdmk.doublejump.jump.feature.restriction.RestrictionResult;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GlidingRestrictionChecker implements RestrictionChecker {

    private final boolean enabled;

    public GlidingRestrictionChecker(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public @NotNull RestrictionResult check(@NotNull Player player) {
        if (!this.enabled) {
            return RestrictionResult.passed();
        }

        if (player.isGliding()) {
            return RestrictionResult.failed(RestrictionDenyReason.PLAYER_GLIDING);
        }

        return RestrictionResult.passed();
    }
}
