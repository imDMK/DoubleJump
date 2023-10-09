package com.github.imdmk.doublejump.configuration.implementation;

import com.github.imdmk.doublejump.command.CommandSettings;
import com.github.imdmk.doublejump.jump.JumpSettings;
import com.github.imdmk.doublejump.notification.configuration.NotificationSettings;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;

@Header({
        "#",
        "# Configuration file for the DoubleJump plugin.",
        "#",
        "# If you have a problem with plugin configuration, please create an issue on the project's github.",
        "# However, if you like the plugin, leave a star for the project on GitHub.",
        "# ",
        "# Support site: https://github.com/imDMK/DoubleJump/issues/new/choose",
        "# GitHub: https://github.com/imDMK/DoubleJump",
        "#",
})
public class PluginConfiguration extends OkaeriConfig {

    @Comment({
            "# Specifies whether to check for a new plug-in version when the administrator joins the server",
            "# I highly recommend enabling this option"
    })
    public boolean checkForUpdate = true;

    @Comment("# Double jump use permission")
    public String doubleJumpUsePermission = "doublejump.use";

    @Comment({"# ", "# Command settings", "# "})
    public CommandSettings commandSettings = new CommandSettings();

    @Comment({"# ", "# Jump settings", "# "})
    public JumpSettings jumpSettings = new JumpSettings();

    @Comment({"# ", "# Notification settings", "# "})
    public NotificationSettings notificationSettings = new NotificationSettings();
}
