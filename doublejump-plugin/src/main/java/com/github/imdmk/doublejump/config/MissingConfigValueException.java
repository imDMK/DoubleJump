package com.github.imdmk.doublejump.config;

import org.jetbrains.annotations.NotNull;

/**
 * Exception thrown when a required configuration value is missing or invalid.
 * <p>
 * Used to indicate a fatal misconfiguration that prevents the plugin from starting or behaving correctly.
 */
public class MissingConfigValueException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public MissingConfigValueException(@NotNull String message) {
        super(message);
    }

    /**
     * Constructs a new exception indicating the specific path and reason for failure.
     *
     * @param path   the configuration path that is invalid or missing
     * @param reason explanation of why the value is invalid or required
     */
    public MissingConfigValueException(@NotNull String path, @NotNull String reason) {
        super("Missing or invalid configuration at '" + path + "': " + reason);
    }
}
