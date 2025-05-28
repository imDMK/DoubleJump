package com.github.imdmk.doublejump.jump.feature.restriction.checker.player;

import com.github.imdmk.doublejump.jump.feature.restriction.RestrictionChecker;
import com.github.imdmk.doublejump.jump.feature.restriction.RestrictionDenyReason;
import com.github.imdmk.doublejump.jump.feature.restriction.RestrictionResult;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PingRestrictionChecker implements RestrictionChecker {

    private static final int PING_THRESHOLD = 250;

    private final boolean enabled;

    public PingRestrictionChecker(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public @NotNull RestrictionResult check(@NotNull Player player) {
        if (!this.enabled) {
            return RestrictionResult.passed();
        }

        int ping = player.getPing();
        if (ping > PING_THRESHOLD) {
            return RestrictionResult.failed(RestrictionDenyReason.PLAYER_LAGGING);
        }

        return RestrictionResult.passed();
    }
}
