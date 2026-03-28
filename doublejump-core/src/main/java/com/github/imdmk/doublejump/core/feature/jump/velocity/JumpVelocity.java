package com.github.imdmk.doublejump.core.feature.jump.velocity;

public record JumpVelocity(double verticalBoost, double horizontalBoost) {

    public static JumpVelocity of(double verticalBoost, double horizontalBoost) {
        return new JumpVelocity(verticalBoost, horizontalBoost);
    }

}
