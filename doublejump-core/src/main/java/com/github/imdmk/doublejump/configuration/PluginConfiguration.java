package com.github.imdmk.doublejump.configuration;

import com.github.imdmk.doublejump.jump.JumpConfiguration;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;

@Header({
        "#",
        "# Configuration file for the DoubleJump plugin.",
        "#",
        "# If you have a problem with plugin configuration, please create an issue on the project's github.",
        "# ",
        "# Support site: https://github.com/imDMK/DoubleJump/issues/new/choose",
        "# Source code: https://github.com/imDMK/DoubleJump",
        "#",
})
public class PluginConfiguration extends OkaeriConfig {

    @Comment({
            "# Specifies whether to check for a new plug-in version when enabling it",
            "# I highly recommend enabling this option"
    })
    public boolean checkForUpdate = true;

    @Comment("# Specifies whether \"/doublejump\" command should be enabled")
    public boolean doubleJumpCommandEnabled = true;

    @Comment("# Permissions")
    public String doubleJumpUsePermission = "doublejump.use";
    public String doubleJumpCommandPermission = "command.doublejump";
    public String doubleJumpEnableForPermission = "command.doublejump.enablefor";
    public String doubleJumpGiveItemPermission = "command.doublejump.giveitem";

    @Comment({
            "# ",
            "# Jump configuration",
            "# "
    })
    public JumpConfiguration jumpConfiguration = new JumpConfiguration();

    @Comment({
            "# ",
            "# Message configuration",
            "# "
    })
    public MessageConfiguration messageConfiguration = new MessageConfiguration();
}
