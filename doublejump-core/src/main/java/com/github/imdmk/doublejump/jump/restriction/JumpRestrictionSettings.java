package com.github.imdmk.doublejump.jump.restriction;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.GameMode;

import java.util.List;

public class JumpRestrictionSettings extends OkaeriConfig {

    @Comment({
            "# Names of regions where the player will not be able to double-jump",
            "# The WorldGuard plugin is required for this feature to work"
    })
    public List<String> disabledRegions = List.of(
            "example-region"
    );

    @Comment("# Names of worlds where the player will not be able to double-jump")
    public List<String> disabledWorlds = List.of(
            "example-world"
    );

    @Comment("# The names of the game modes during which the player will not be able to double-jump")
    public List<GameMode> disabledGameModes = List.of(
            GameMode.SPECTATOR,
            GameMode.CREATIVE
    );

}
