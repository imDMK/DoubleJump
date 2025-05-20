package com.github.imdmk.doublejump.feature.message;

import com.eternalcode.multification.notice.Notice;
import com.eternalcode.multification.notice.resolver.NoticeResolverDefaults;
import com.eternalcode.multification.okaeri.MultificationSerdesPack;
import com.github.imdmk.doublejump.configuration.ConfigSection;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import org.jetbrains.annotations.NotNull;

public class MessageConfiguration extends ConfigSection {

    @Comment("Sent when double jump is successfully enabled.")
    public Notice jumpEnabled = Notice.chat("<green>Enable double jump mode!</green>");

    @Comment("Sent when double jump is successfully disabled.")
    public Notice jumpDisabled = Notice.chat("<red>Disable double jump mode!</red>");

    @Comment("Sent when double jump is blocked due to a restricted world.")
    public Notice worldRestricted = Notice.chat("<red>You are in disabled world!</red>");

    @Comment("Sent when double jump is blocked due to the current game mode.")
    public Notice gameModeRestricted = Notice.chat("<red>You have disabled gamemode!</red>");

    @Comment("Sent when double jump is blocked due to restricted permissions.")
    public Notice jumpPermissionRequired = Notice.chat("<red>You don't have permission to activate double jump!</red>");

    @Comment("Generic error message when an unexpected issue occurs.")
    public Notice errorOccurred = Notice.chat("<red>An error occurred while trying to perform this action. Please join on server again!</red>");

    @Comment({
            "Sent when a command is executed without the required permissions.",
            "{PERMISSIONS} - Displays the required permission nodes."
    })
    public Notice commandNoPermission = Notice.chat("<red>Missing permissions: <dark_red>{PERMISSIONS}<dark_gray>.");

    @Comment({
            "Sent when a command is executed with incorrect arguments.",
            "{USAGE} - Displays the correct command usage."
    })
    public Notice commandInvalidUsage = Notice.chat("<red>Invalid usage: <dark_red>{USAGE}<dark_gray>.");

    @Comment("Header message displayed when showing multiple valid command usages.")
    public Notice commandUsageHeader = Notice.chat("<red>Invalid usage:");

    @Comment({
            "Entry format used when listing valid command usages.",
            "{USAGE} - Displays the correct command usage."
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
