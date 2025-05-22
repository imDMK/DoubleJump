package com.github.imdmk.doublejump.feature.message;

import com.eternalcode.multification.notice.Notice;
import com.eternalcode.multification.notice.resolver.NoticeResolverDefaults;
import com.eternalcode.multification.okaeri.MultificationSerdesPack;
import com.github.imdmk.doublejump.configuration.ConfigSection;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import org.jetbrains.annotations.NotNull;

public class MessageConfiguration extends ConfigSection {

    @Comment("# Sent when all plugin configuration files have been successfully reloaded. " +
            "# Note that some features may require a full restart to take effect.")
    public Notice reload = Notice.chat("<green>The plugin configuration files have been reloaded.</green>");

    @Comment("# Sent when an error occurs during plugin configuration reload. " +
            "# Please check the console for details.")
    public Notice reloadError = Notice.chat("<red>Failed to reload plugin configuration files. Please check the console.</red>");

    @Comment("# Sent when double jump is successfully enabled.")
    public Notice jumpEnabled = Notice.chat("<green>Double jump mode enabled!</green>");

    @Comment("# Sent when all restrictions are lifted and the player can now double jump again.")
    public Notice jumpAvailable = Notice.actionbar("<green>You can now double jump!");

    @Comment("# Sent when double jump is successfully disabled.")
    public Notice jumpDisabled = Notice.chat("<red>Double jump mode disabled!</red>");

    @Comment("# Sent when double jump is blocked due to the player being in a restricted world.")
    public Notice worldRestricted = Notice.chat("<red>You are in a disabled world!</red>");

    @Comment("# Sent when double jump is blocked due to the current game mode.")
    public Notice gameModeRestricted = Notice.chat("<red>Your current game mode disables double jump!</red>");

    @Comment("# Sent when double jump is blocked due to cooldown/delay.")
    public Notice jumpDelay = Notice.actionbar("<red>You must wait {TIME} before your next jump!");

    @Comment("# Sent when double jump is blocked due to high player ping.")
    public Notice playerLagging = Notice.chat("<red>Your ping is too high to use double jump!");

    @Comment("# Sent when double jump is blocked due to insufficient permissions.")
    public Notice jumpPermissionRequired = Notice.chat("<red>You don't have permission to activate double jump!</red>");

    @Comment("# Generic error message when an unexpected issue occurs.")
    public Notice errorOccurred = Notice.chat("<red>An error occurred while performing this action. Please rejoin the server!</red>");

    @Comment({
            "# Sent when a command is executed without required permissions.",
            "# {PERMISSIONS} - Lists the required permission nodes."
    })
    public Notice commandNoPermission = Notice.chat("<red>Missing permissions: <dark_red>{PERMISSIONS}<dark_gray>.");

    @Comment({
            "# Sent when a command is executed with invalid arguments.",
            "# {USAGE} - Displays the correct command usage."
    })
    public Notice commandInvalidUsage = Notice.chat("<red>Invalid usage: <dark_red>{USAGE}<dark_gray>.");

    @Comment("# Header message shown when listing multiple valid command usages.")
    public Notice commandUsageHeader = Notice.chat("<red>Invalid usage:");

    @Comment({
            "# Entry format for each valid command usage.",
            "# {USAGE} - Displays the correct command usage."
    })
    public Notice commandUsageEntry = Notice.chat("<dark_gray>- <red>{USAGE}");

    @Override
    public @NotNull OkaeriSerdesPack getSerdesPack() {
        return registry -> {
            registry.register(new MultificationSerdesPack(NoticeResolverDefaults.createRegistry()));
        };
    }

    @Override
    public @NotNull String getFileName() {
        return "messageConfiguration.yml";
    }
}
