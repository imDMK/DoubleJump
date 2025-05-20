package com.github.imdmk.doublejump.feature.jump.restriction.result.checker;

import com.github.imdmk.doublejump.feature.jump.restriction.result.RestrictionDenyReason;
import com.github.imdmk.doublejump.feature.jump.restriction.result.RestrictionResult;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

public class PermissionRestrictionChecker implements RestrictionChecker {

    private final Set<String> whitelist;
    private final Set<String> blacklist;

    public PermissionRestrictionChecker(
            @NotNull Set<String> whitelist,
            @NotNull Set<String> blacklist
    ) {
        this.whitelist = Objects.requireNonNull(whitelist, "whitelist cannot be null");
        this.blacklist = Objects.requireNonNull(blacklist, "blacklist cannot be null");
    }

    @Override
    public @NotNull RestrictionResult check(@NotNull Player player) {
        for (String deniedPerm : this.blacklist) {
            if (player.hasPermission(deniedPerm)) {
                return RestrictionResult.failed(RestrictionDenyReason.PERMISSION_REQUIRED);
            }
        }

        if (!this.whitelist.isEmpty()) {
            for (String allowedPerm : this.whitelist) {
                if (player.hasPermission(allowedPerm)) {
                    return RestrictionResult.passed();
                }
            }

            return RestrictionResult.failed(RestrictionDenyReason.PERMISSION_REQUIRED);
        }

        return RestrictionResult.passed();
    }
}

