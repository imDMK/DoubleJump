package me.dmk.doublejump.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;

@Header("#")
@Header("# Configuration for double jump plugin")
@Header("#")
public class PluginConfiguration extends OkaeriConfig {

    @Comment("# Booleans")
    @Comment("# Check if you are using the latest version when enabling the plugin?")
    public boolean checkForUpdate = true;
    public boolean doubleJumpCommandEnabled = true;

    @Comment("# Permissions")
    public String doubleJumpUsePermission = "doublejump.use";
    public String doubleJumpCommandPermission = "command.doublejump";
    public String doubleJumpEnableForPermission = "command.doublejump.enablefor";
    public String doubleJumpGiveItemPermission = "command.doublejump.giveitem";

    @Comment("Jump configuration")
    public JumpConfiguration jumpConfiguration = new JumpConfiguration();

    @Comment({
            "# Message configuration",
            "# Notification types: CHAT, ACTIONBAR, TITLE, SUBTITLE, DISABLED",
    })
    public MessageConfiguration messageConfiguration = new MessageConfiguration();
}
