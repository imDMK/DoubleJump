package com.github.imdmk.doublejump.infrastructure.message;

import com.eternalcode.multification.notice.Notice;
import com.eternalcode.multification.notice.resolver.NoticeResolverDefaults;
import com.eternalcode.multification.okaeri.MultificationSerdesPack;
import com.github.imdmk.doublejump.configuration.ConfigSection;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import org.jetbrains.annotations.NotNull;

@Header({
        "# ",
        "# DoubleJump Premium - Message Configuration",
        "# Configure the all messages below.",
        "# ",
        "# Enjoying the plugin? Please leave a review on SpigotMC!",
        "# Support development: https://github.com/sponsors/imDMK",
        "# "
})
public class MessageConfiguration extends ConfigSection {

    @Comment("# Sent when all plugin configuration files have been successfully reloaded.")
    public Notice reload = Notice.chat("<green>The plugin configuration files have been reloaded. <yellow>Note that some features may require a full restart to take effect.");

    @Comment("# Sent when an error occurs during plugin configuration reload.")
    public Notice reloadError = Notice.chat("<red>Failed to reload plugin configuration files. Please check the console.");

    @Comment("# Message sent when double jump mode is auto-enabled for a player (e.g., on join).")
    public Notice autoJumpEnabled = Notice.chat("<green>Double jump mode enabled (by joining)!");

    @Comment("# Message sent when double jump mode is successfully enabled for the executing player.")
    public Notice jumpEnabled = Notice.chat("<green>Double jump mode enabled!");

    @Comment("# Message sent when double jump mode is successfully enabled for the target player.")
    public Notice jumpEnabledTarget = Notice.chat("<green>Double jump mode enabled for the player!");

    @Comment("# Message sent when double jump mode is successfully disabled for the executing player.")
    public Notice jumpDisabled = Notice.chat("<red>Double jump mode disabled!");

    @Comment("# Message sent when double jump mode is successfully disabled for the target player.")
    public Notice jumpDisabledTarget = Notice.chat("<red>Double jump mode disabled for the player!");

    @Comment("# Message sent when all restrictions are lifted and the player can double jump again.")
    public Notice jumpAvailable = Notice.actionbar("<rainbow>You can now double jump!");

    @Comment("# Sent when double jump is blocked because the player is in a restricted world.")
    public Notice worldRestricted = Notice.chat("<red>You are in a disabled world!");

    @Comment("# Sent when double jump is blocked because the player is in a restricted WorldGuard region.")
    public Notice regionRestricted = Notice.chat("<red>You are in a disabled region!");

    @Comment("# Sent when double jump is blocked due to the player's current game mode.")
    public Notice gameModeRestricted = Notice.chat("<red>Your current game mode disables double jump!");

    @Comment("# Sent when double jump is blocked due to cooldown or delay.")
    public Notice jumpDelay = Notice.actionbar("<red>You must wait {TIME} before your next jump!");

    @Comment("# Message sent when double jump is blocked because the player is gliding (e.g., with elytra)")
    public Notice blockedWhileGliding = Notice.actionbar("<red>You cannot use double jump while gliding.");

    @Comment("# Sent when double jump is blocked due to high player ping.")
    public Notice playerLagging = Notice.chat("<red>Your ping is too high to use double jump!");

    @Comment("# Sent when double jump is blocked due to insufficient permissions.")
    public Notice jumpPermissionRequired = Notice.chat("<red>You don't have permission to activate double jump!");

    @Comment("# Sent when a double jump item is successfully added to the target player's inventory.")
    public Notice jumpItemAdded = Notice.chat("<green>Added double jump item to player!");

    @Comment("# Sent when a single double jump item is successfully removed from the player's inventory.")
    public Notice jumpItemRemovedSingle = Notice.chat("<green>Removed a double jump item from the player's inventory.");

    @Comment("# Sent when all double jump items are successfully removed from the player's inventory.")
    public Notice jumpItemRemovedAll = Notice.chat("<green>Removed all double jump items from the player's inventory.");

    @Comment("# Sent when no double jump item is found in the player's inventory.")
    public Notice jumpItemNotFound = Notice.chat("<red>No double jump items found in the player's inventory.");

    @Comment("# Generic error message sent when an unexpected issue occurs.")
    public Notice errorOccurred = Notice.chat("<red>An error occurred while performing this action. Please rejoin the server!");

    @Comment({
            "# Sent when a command is executed without the required permissions.",
            "# {PERMISSIONS} - Lists the required permission nodes."
    })
    public Notice noPermission = Notice.chat("<red>Missing permissions: <dark_red>{PERMISSIONS}<dark_gray>.");

    @Comment({
            "# Sent when a command is executed with invalid arguments.",
            "# {USAGE} - Displays the correct command usage."
    })
    public Notice invalidUsage = Notice.chat("<red>Invalid usage: <dark_red>{USAGE}<dark_gray>.");

    @Comment("# Header message shown when listing multiple valid command usages.")
    public Notice invalidUsageHeader = Notice.chat("<red>Invalid usage:");

    @Comment({
            "# Entry format for each valid command usage.",
            "# {USAGE} - Displays the correct command usage."
    })
    public Notice invalidUsageEntry = Notice.chat("<dark_gray>- <red>{USAGE}");

    @Override
    public @NotNull OkaeriSerdesPack getSerdesPack() {
        return registry -> registry.register(new MultificationSerdesPack(NoticeResolverDefaults.createRegistry()));
    }

    @Override
    public @NotNull String getFileName() {
        return "messageConfiguration.yml";
    }
}
