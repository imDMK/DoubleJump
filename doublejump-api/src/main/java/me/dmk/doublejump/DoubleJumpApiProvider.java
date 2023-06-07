package me.dmk.doublejump;

public final class DoubleJumpApiProvider {

    private static DoubleJumpApi api;

    private DoubleJumpApiProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    public static DoubleJumpApi get() {
        DoubleJumpApi doubleJumpApi = DoubleJumpApiProvider.api;
        if (doubleJumpApi == null) {
            throw new IllegalStateException("The DoubleJump API isn't registered.");
        }

        return doubleJumpApi;
    }

    static void register(DoubleJumpApi doubleJumpApi) {
        DoubleJumpApiProvider.api = doubleJumpApi;
    }

    static void unregister() {
        DoubleJumpApiProvider.api = null;
    }
}
