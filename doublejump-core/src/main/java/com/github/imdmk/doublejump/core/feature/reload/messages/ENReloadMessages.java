package com.github.imdmk.doublejump.core.feature.reload.messages;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;

public final class ENReloadMessages
        extends OkaeriConfig
        implements ReloadMessages {

    Notice reloaded = Notice.chat("<dark_gray>• <green>Reloaded DoubleJump configuration");

    @Override
    public Notice reloaded() {
        return reloaded;
    }
}
