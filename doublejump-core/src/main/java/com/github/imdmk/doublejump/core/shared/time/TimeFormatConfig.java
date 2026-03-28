package com.github.imdmk.doublejump.core.shared.time;

import com.github.imdmk.doublejump.core.config.ConfigSection;
import com.github.imdmk.doublejump.core.injector.annotations.ConfigFile;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;

@ConfigFile
public final class TimeFormatConfig extends ConfigSection {

    @Comment({"#", "# Suffix for seconds", "#"})
    public String seconds = "s";

    @Comment({"#", "# Suffix for minutes", "#"})
    public String minutes = "m";

    @Comment({"#", "# Suffix for hours", "#"})
    public String hours = "h";

    @Override
    public OkaeriSerdesPack serdesPack() {
        return registry -> {};
    }

    @Override
    public String fileName() {
        return "timeFormatConfig.yml";
    }
}
