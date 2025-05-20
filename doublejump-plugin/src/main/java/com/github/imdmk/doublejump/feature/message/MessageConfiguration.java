package com.github.imdmk.doublejump.feature.message;

import com.eternalcode.multification.notice.Notice;
import com.eternalcode.multification.notice.resolver.NoticeResolverDefaults;
import com.eternalcode.multification.okaeri.MultificationSerdesPack;
import com.github.imdmk.doublejump.configuration.ConfigSection;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import org.jetbrains.annotations.NotNull;

public class MessageConfiguration extends ConfigSection {

    public Notice enabled = Notice.chat("<green>Enable double jump mode!</green>");

    public Notice disabled = Notice.chat("<red>Disable double jump mode!</red>");

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
