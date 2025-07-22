package com.github.imdmk.doublejump.jump.feature.restriction.cooldown;

import com.github.imdmk.doublejump.jump.JumpConfig;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.time.Duration;
import java.util.Map;

/**
 * Service responsible for determining cooldown durations for players based on their permissions.
 *
 * <p>Cooldowns are defined in the {@link JumpConfig#cooldowns} map, where keys are permission strings
 * and values are the cooldown durations. The "default" key is used when no other permission matches.</p>
 */
public class CooldownRestrictionService {

    private static final String DEFAULT_IDENTIFIER = "default";

    @Inject private JumpConfig config;

    /**
     * Retrieves the cooldown duration for the given player based on their permissions.
     *
     * <p>Iterates through all configured cooldown entries and returns the first matching one
     * where the player has the corresponding permission. If no permission matches,
     * the default cooldown is returned.</p>
     *
     * @param player the player whose cooldown is being determined
     * @return the cooldown duration applicable to the player
     */
    public @NotNull Duration forPlayer(@NotNull Player player) {
        for (Map.Entry<String, Duration> entry : this.config.cooldowns.entrySet()) {
            String permission = entry.getKey();

            if (this.isCustomPermissionKey(permission) && player.hasPermission(permission)) {
                return entry.getValue();
            }
        }

        return this.defaultCooldown();
    }

    /**
     * Returns the default cooldown duration defined in the configuration.
     *
     * @return the default cooldown duration
     */
    public @NotNull Duration defaultCooldown() {
        return this.config.cooldowns.get(DEFAULT_IDENTIFIER);
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
