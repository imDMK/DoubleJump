package com.github.imdmk.doublejump;

import org.jetbrains.annotations.NotNull;

/**
 * Static access point for the {@link DoubleJumpApi}.
 * Acts as a global registry for the current instance.
 * <p>
 * Not thread-safe.
 */
public class DoubleJumpApiProvider {

    private static DoubleJumpApi DOUBLE_JUMP_API;

    private DoubleJumpApiProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    /**
     * Returns the registered {@link DoubleJumpApi}.
     *
     * @return the registered API
     * @throws IllegalStateException if the API is not yet registered
     */
    public synchronized static DoubleJumpApi get() {
        if (DOUBLE_JUMP_API == null) {
            throw new IllegalStateException("The SpentTimeApi isn't registered.");
        }

        return DOUBLE_JUMP_API;
    }

    /**
     * Registers the {@link DoubleJumpApi} instance.
     *
     * @param spentTimeApi the API instance to register
     * @throws IllegalStateException if already registered
     */
    static synchronized void register(@NotNull DoubleJumpApi spentTimeApi) {
        if (DOUBLE_JUMP_API != null) {
            throw new IllegalStateException("The SpentTimeApi is already registered.");
        }

        DOUBLE_JUMP_API = spentTimeApi;
    }

    /**
     * Forces to register the {@link DoubleJumpApi} instance.
     */
    static void forceRegister(@NotNull DoubleJumpApi api) {
        DOUBLE_JUMP_API = api;
    }

    /**
     * Unregisters the {@link DoubleJumpApi}.
     *
     * @throws IllegalStateException if no API was registered
     */
    static synchronized void unregister() {
        if (DOUBLE_JUMP_API == null) {
            throw new IllegalStateException("The SpentTimeApi isn't registered.");
        }

        DOUBLE_JUMP_API = null;
    }
}
