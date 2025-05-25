package com.github.imdmk.doublejump.jump;

/**
 * Immutable holder for double jump velocity boost properties.
 * <p>
 * Defines multipliers for both horizontal and vertical components of the player's velocity during a double jump.
 * These values determine how far and how high a player will be propelled.
 * </p>
 *
 * @param horizontalBoost multiplier applied to the player's horizontal velocity during a double jump
 * @param verticalBoost   multiplier applied to the player's vertical velocity during a double jump
 */
public record JumpVelocity(double horizontalBoost, double verticalBoost) {

    /**
     * Factory method to create a new {@link JumpVelocity} instance with the given boost values.
     *
     * @param horizontalBoost the multiplier for horizontal velocity
     * @param verticalBoost   the multiplier for vertical velocity
     * @return a new {@link JumpVelocity} instance
     */
    public static JumpVelocity of(double horizontalBoost, double verticalBoost) {
        return new JumpVelocity(horizontalBoost, verticalBoost);
    }
}
