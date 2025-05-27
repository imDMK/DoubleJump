package com.github.imdmk.doublejump.configuration;

import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

@Header({
        "# ",
        "# DoubleJump Premium",
        "# Thanks for purchasing this plugin!",
        "#",
        "# If you encounter any issues or need assistance,",
        "# feel free to contact me:",
        "# ",
        "# Discord: imdmk",
        "# Email: dominiks8318@gmail.com",
        "#",
        "# If you enjoy the plugin, please leave a review on",
        "# SpigotMC — your support means a lot!",
        "#",
        "# Support development: https://github.com/sponsors/imDMK",
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
