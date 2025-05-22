package com.github.imdmk.doublejump.feature.jump.restriction.result.checker;

import com.github.imdmk.doublejump.jump.restriction.RestrictionChecker;
import com.github.imdmk.doublejump.jump.restriction.RestrictionDenyReason;
import com.github.imdmk.doublejump.jump.restriction.RestrictionResult;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

public class PermissionRestrictionChecker implements RestrictionChecker {

    private final Set<String> allowedPermissions;

    public PermissionRestrictionChecker(@NotNull Set<String> allowedPermissions) {
        this.allowedPermissions = Objects.requireNonNull(allowedPermissions, "required cannot be null");
    }

    @Override
    public @NotNull RestrictionResult check(@NotNull Player player) {
        if (!this.allowedPermissions.isEmpty()) {
            for (String permission : this.allowedPermissions) {
                if (player.hasPermission(permission)) {
                    return RestrictionResult.passed();
                }
            }

            return RestrictionResult.failed(RestrictionDenyReason.PERMISSION_REQUIRED);
        }

        return RestrictionResult.passed();
    }
}
