package com.github.imdmk.doublejump.placeholder;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

public class PlaceholderSettings extends OkaeriConfig {

    @Comment({
            "# Message shown after using the placeholder if it returns true.",
            "# Example:",
            "# whenTrueMessage: 'Available'",
            "# Placeholder 'jump-player-is-delay' will return 'Available' when player doesn't have delay"
    })
    public String whenTrueMessage = "yes";

    @Comment({
            "# Message shown after using the placeholder if it returns false.",
            "# Example:",
            "# whenFalseMessage: 'Cooldown'",
            "# Placeholder 'jump-player-is-delay' will return 'Cooldown' when player have delay"
    })
    public String whenFalseMessage = "no";
}
