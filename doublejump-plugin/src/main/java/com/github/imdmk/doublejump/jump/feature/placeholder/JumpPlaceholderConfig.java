package com.github.imdmk.doublejump.jump.feature.placeholder;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

public class JumpPlaceholderConfig extends OkaeriConfig {

    @Comment({
            "# Enable or disable PlaceholderAPI support for DoubleJump placeholders.",
            "# Set to false to disable all placeholders."
    })
    public boolean enabled = true;

    @Comment({
            "# Text to display when the player's cooldown has expired or is not set.",
            "# Example: 'ready', 'now', '✓'"
    })
    public String cooldownExpiredText = "ready";

    @Comment({
            "# Text to display when the player is allowed to jump.",
            "# Example: 'allowed', 'yes', '✔'"
    })
    public String jumpAllowedText = "allowed";

    @Comment({
            "# Text to display when the player is NOT allowed to jump.",
            "# Example: 'denied', 'no', '✘'"
    })
    public String jumpNotAllowedText = "denied";
}
