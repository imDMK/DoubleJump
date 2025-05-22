package com.github.imdmk.doublejump.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract base class for configuration sections.
 * <p>
 * Extends {@link OkaeriConfig} to provide common configuration functionality.
 * Subclasses must specify the serialization/deserialization pack and
 * the configuration file name.
 * </p>
 */
public abstract class ConfigSection extends OkaeriConfig {

    /**
     * Returns the {@link OkaeriSerdesPack} used to serialize and deserialize
     * this configuration section.
     *
     * @return the serialization/deserialization pack, never null
     */
    public abstract @NotNull OkaeriSerdesPack getSerdesPack();

    /**
     * Returns the file name for this configuration section.
     * This name is used to save and load the configuration from disk.
     *
     * @return the configuration file name, never null
     */
    public abstract @NotNull String getFileName();
}
