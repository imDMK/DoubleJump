package com.github.imdmk.doublejump.jump.sound;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.Sound;

import java.util.List;

public class JumpSoundSettings extends OkaeriConfig {

    @Comment("# Specifies whether to enable double jump sound")
    public boolean enabled = true;

    @Comment("# Specifies whether nearby players should hear the jump sound")
    public boolean playNearbyEnabled = true;

    @Comment("# Specifies the distance in X coordinates at which the jump sound should be played")
    public double playNearbyX = 5;

    @Comment("# Specifies the distance in Y coordinates at which the jump sound should be played")
    public double playNearbyY = 5;

    @Comment("# Specifies the distance in Z coordinates at which the jump sound should be played")
    public double playNearbyZ = 5;

    @Comment("# List of available sounds: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html")
    public List<JumpSound> sounds = List.of(
            new JumpSound(Sound.ENTITY_EXPERIENCE_BOTTLE_THROW, 0.20F, 1),
            new JumpSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.15F, 1)
    );

}
