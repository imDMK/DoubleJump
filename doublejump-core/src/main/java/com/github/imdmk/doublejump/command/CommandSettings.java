package com.github.imdmk.doublejump.command;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

public class CommandSettings extends OkaeriConfig {

    @Comment("# Specifies whether \"/doublejump\" command should be enabled")
    public boolean doubleJumpEnabled = true;
}
