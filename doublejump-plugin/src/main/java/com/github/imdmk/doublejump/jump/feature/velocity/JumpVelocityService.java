package com.github.imdmk.doublejump.jump.feature.velocity;

import com.github.imdmk.doublejump.jump.JumpConfig;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;
import org.panda_lang.utilities.inject.annotations.PostConstruct;

import java.util.Map;

public class JumpVelocityService {

    private static final String DEFAULT_IDENTIFIER = "default";

    @Inject private JumpConfig jumpConfig;

    @PostConstruct
    private void postConstruct() {
        if (!this.jumpConfig.velocities.containsKey(DEFAULT_IDENTIFIER)) {
            throw new IllegalStateException("Missing required 'default' JumpProperties configuration.");
        }
    }

    /**
     * Returns the JumpProperties for the given player based on their permissions.
     * If no matching permission is found, the default properties are returned.
     *
     * @param player the player to check permissions for
     * @return the matching JumpProperties instance
     */
    public @NotNull JumpVelocity forPlayer(@NotNull Player player) {
        for (Map.Entry<String, JumpVelocity> entry : this.jumpConfig.velocities.entrySet()) {
            String permission = entry.getKey();

            if (this.isCustomPermissionKey(permission) && player.hasPermission(permission)) {
                return entry.getValue();
            }
        }

        return this.defaultProperties();
    }

    /**
     * Returns the default {@link JumpVelocity} defined in the configuration.
     * This is used as a fallback when a player has no matching permission-based properties.
     *
     * @return the default jump properties
     * @throws IllegalStateException if the default properties are missing
     */
    public @NotNull JumpVelocity defaultProperties() {
        return this.jumpConfig.velocities.get(DEFAULT_IDENTIFIER);
    }

    /**
     * Checks whether the given permission key is considered a custom (non-default) key.
     * Used to filter out the special "default" entry during permission lookups.
     *
     * @param permission the permission key to check
     * @return true if the permission is not the default identifier, false otherwise
     */
    private boolean isCustomPermissionKey(@NotNull String permission) {
        return !permission.equals(DEFAULT_IDENTIFIER);
    }
}
