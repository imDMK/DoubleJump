package com.github.imdmk.doublejump.core.feature.jump.rule;

public enum JumpResult {
    ALLOWED(false),
    CANCELLED(false),
    COOLDOWN(false),
    BLOCKED_BY_COMBAT(true),
    BLOCKED_BY_LAG(true),
    BLOCKED_BY_WORLD(true),
    BLOCKED_BY_GAMEMODE(true),
    BLOCKED_BY_REGION(true),
    BLOCKED_BY_GLIDING(true),
    BLOCKED_BY_FLUID(false),
    BLOCKED_BY_VEHICLE(false);

    private final boolean disableJumpMode;

    JumpResult(boolean disableJumpMode) {
        this.disableJumpMode = disableJumpMode;
    }

    public boolean isAllowed() {
        return this == ALLOWED;
    }

    public boolean isDisableJumpMode() {
        return disableJumpMode;
    }
}
