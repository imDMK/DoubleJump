package com.github.imdmk.doublejump.configuration;

import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

@Header({
        "#",
        "# Configuration file for the DoubleJump (free version) plugin.",
        "#",
        "# If you have a problem with plugin configuration, please create an issue on the project's github.",
        "# However, if you like the plugin, leave a star for the project on GitHub.",
        "# ",
        "# Support site: https://github.com/imDMK/DoubleJump/issues/new/choose",
        "# GitHub: https://github.com/imDMK/DoubleJump",
        "#",
})
public class PluginConfiguration extends ConfigSection {

    @Comment("# Check for plugin update and send notification after administrator join to server?")
    public boolean checkUpdate = true;

    @Comment("# How often should the plugin check for updates? Recommended value: 1 day")
    public Duration updateInterval = Duration.ofDays(1);

    @Override
    public @NotNull OkaeriSerdesPack getSerdesPack() {
        return registry -> {
            registry.register(new SerdesCommons());
        };
    }

    @Override
    public @NotNull String getFileName() {
        return "pluginConfiguration.yml";
    }
}
