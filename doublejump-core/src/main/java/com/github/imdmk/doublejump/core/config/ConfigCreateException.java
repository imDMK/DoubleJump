package com.github.imdmk.doublejump.core.config;

public class ConfigCreateException extends RuntimeException {

    public ConfigCreateException(String message) {
        super(message);
    }

    public ConfigCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigCreateException(Throwable cause) {
        super(cause);
    }
}
