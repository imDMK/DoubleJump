package com.github.imdmk.doublejump.command;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

public class CommandSettings extends OkaeriConfig {

    @Comment("# Specifies whether \"/doublejump\" command should be enabled")
    public boolean doubleJumpEnabled = true;

    @Comment("# General permission for double jump command")
    public String doubleJumpPermission = "command.doublejump";

    @Comment("# Permission for \"/doublejump enable(-disable)-for\" command")
    public String doubleJumpForPermission = "command.doublejump.enablefor";

    @Comment("# Permission for \"/doublejump item-give(-remove)\" command")
    public String doubleJumpItemPermission = "command.doublejump.giveitem";
}
