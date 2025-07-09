package com.github.imdmk.doublejump.command.configurator;

import com.github.imdmk.doublejump.config.ConfigSection;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Header({
        "# ",
        "# DoubleJump Premium - Database Configuration",
        "# ",
        "# This file allows you to configure commands.",
        "# Example of how to rename or disable commands and subcommands.",
        "# ",
        "# Enjoying the plugin? Please leave a review on SpigotMC!",
        "# Support development: https://github.com/sponsors/imDMK",
        "# "
})
public class CommandConfig extends ConfigSection {

    @Comment("# Enable or disable the command configurator feature.")
    public boolean enabled = false;

    @Comment({
            "# Commands map: each command can be configured individually.",
            "# You can rename commands, enable/disable them, add aliases and permissions.",
            "# Nested subCommands can also be configured similarly."
    })
    public Map<String, Command> commandsToEdit = new HashMap<>(Map.of(
            "doublejump", new Command(
                    "doublejump",
                    true,
                    List.of("dj"),
                    List.of("command.doublejump"),
                    Map.of(
                            "visual", new SubCommand(
                                    "visual",
                                    true,
                                    List.of("settings"),
                                    List.of("command.doublejump.visual")
                            )
                    )
            )
    ));

    /**
     * Retrieve a command configuration by name.
     * @param name Command key
     * @return Optional containing the command if present
     */
    public Optional<Command> getCommand(String name) {
        return Optional.ofNullable(this.commandsToEdit.get(name));
    }

    @Override
    public @NotNull OkaeriSerdesPack getSerdesPack() {
        // No custom serializers needed for now
        return registry -> {};
    }

    @Override
    public @NotNull String getFileName() {
        return "commandConfig.yml";
    }
}
