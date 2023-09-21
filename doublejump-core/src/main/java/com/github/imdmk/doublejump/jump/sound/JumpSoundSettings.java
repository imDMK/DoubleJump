package com.github.imdmk.doublejump.jump.sound;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.Sound;

import java.util.List;

public class JumpSoundSettings extends OkaeriConfig {

    @Comment("# Specifies whether to enable double jump sound")
    public boolean enabled = true;

    @Comment("# List of available sounds: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html")
    public List<JumpSound> sounds = List.of(
            new JumpSound(Sound.ENTITY_EXPERIENCE_BOTTLE_THROW, 0.20F, 1),
            new JumpSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.15F, 1)
    );

}
