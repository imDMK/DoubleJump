package com.github.imdmk.doublejump;

public final class DoubleJumpApiProvider {

    private static DoubleJumpApi DOUBLE_JUMP_API;

    private DoubleJumpApiProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    public static DoubleJumpApi get() {
        if (DOUBLE_JUMP_API == null) {
            throw new IllegalStateException("The DoubleJump API isn't registered.");
        }

        return DOUBLE_JUMP_API;
    }

    static void register(DoubleJumpApi doubleJumpApi) {
        if (DOUBLE_JUMP_API != null) {
            throw new IllegalStateException("The DoubleJump API is already registered.");
        }

        DOUBLE_JUMP_API = doubleJumpApi;
    }

    static void unregister() {
        if (DOUBLE_JUMP_API == null) {
            throw new IllegalStateException("The DoubleJump API isn't registered.");
        }

        DOUBLE_JUMP_API = null;
    }
}
