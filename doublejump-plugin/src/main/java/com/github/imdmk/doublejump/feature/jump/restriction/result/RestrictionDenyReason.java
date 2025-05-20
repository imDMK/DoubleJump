package com.github.imdmk.doublejump.feature.jump.restriction.result;

/**
 * Enumerates reasons why a player may be restricted from using double jump.
 * <p>
 * These reasons are used in {@link com.github.imdmk.doublejump.feature.jump.restriction.result.RestrictionResult}
 * to provide context when a restriction check fails.
 * </p>
 */
public enum RestrictionDenyReason {

    /**
     * The player's current world is not allowed for double jump.
     */
    WORLD_DISABLED,

    /**
     * The region (e.g., WorldGuard) the player is currently in disallows double jump.
     */
    REGION_DISABLED,

    /**
     * The player's current game mode is not permitted for double jump.
     */
    GAME_MODE_BLOCKED,

    /**
     * The player lacks the necessary permission to use double jump.
     */
    PERMISSION_REQUIRED,

    /**
     * A general fallback reason when none of the above apply.
     */
    OTHER
}
