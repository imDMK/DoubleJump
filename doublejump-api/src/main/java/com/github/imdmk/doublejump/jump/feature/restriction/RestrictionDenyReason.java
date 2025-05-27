package com.github.imdmk.doublejump.jump.feature.restriction;

/**
 * Enumerates reasons why a player may be restricted from using double jump.
 * <p>
 * These reasons are used in {@link RestrictionResult}
 * to provide context when a restriction check fails.
 * </p>
 */
public enum RestrictionDenyReason {

    /**
     * The player's current world is not allowed for double jump.
     * Hard restriction: true
     */
    WORLD_DISABLED(true),

    /**
     * The player's current game mode is not permitted for double jump.
     * Hard restriction: true
     */
    GAME_MODE_BLOCKED(true),

    /**
     * The player lacks the necessary permission to use double jump.
     * Hard restriction: true
     */
    PERMISSION_REQUIRED(true),

    /**
     * The player's network connection is unstable (ping is too high).
     * Hard restriction: true
     */
    PLAYER_LAGGING(true),

    /**
     * The player is attempting to double jump before the configured cooldown
     * period has elapsed since the last jump.
     * Hard restriction: false
     */
    JUMP_DELAY(false);

    /**
     * Indicates whether the restriction is considered "hard",
     * meaning it should immediately and forcibly disable double jump functionality.
     * <p>
     * Hard restrictions typically include world constraints, permission issues,
     * game mode blocks, or network conditions that prevent consistent gameplay.
     * </p>
     */
    private final boolean hardRestriction;

    RestrictionDenyReason(boolean hardRestriction) {
        this.hardRestriction = hardRestriction;
    }

    public boolean isHardRestriction() {
        return this.hardRestriction;
    }

}
