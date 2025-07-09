package com.github.imdmk.doublejump.config;

public final class ConfigLoadException extends RuntimeException {
    public ConfigLoadException(Throwable cause) {
        super("Failed to load configuration", cause);
    }

    public ConfigLoadException(String message) {
        super(message);
    }

    public ConfigLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
