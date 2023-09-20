package com.github.imdmk.doublejump.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

import java.util.List;

public class CommandConfiguration extends OkaeriConfig {

    @Comment("# Specifies whether \"/doublejump\" command should be enabled")
    public boolean doubleJumpEnabled = true;

    @Comment("# Aliases of double jump command")
    public List<String> doubleJumpAliases = List.of("dj");

    @Comment("# Permissions")
    public String doubleJumpPermission = "command.doublejump";
    public String doubleJumpEnableForPermission = "command.doublejump.enablefor";
    public String doubleJumpGiveItemPermission = "command.doublejump.giveitem";
}
