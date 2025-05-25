package com.github.imdmk.doublejump.feature.jump.sound.configuration;

import com.github.imdmk.doublejump.feature.jump.sound.JumpSound;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.Sound;

import java.util.List;

public class JumpSoundConfiguration extends OkaeriConfig {

    @Comment("# Enables or disables the double jump sounds.")
    public boolean enabled = true;

    @Comment("List of sounds played when a player uses double jump.")
    public List<JumpSound> jump = List.of(
            JumpSound.builder()
                    .sound(Sound.ENTITY_PLAYER_LEVELUP)
                    .volume(0.3F)
                    .pitch(0.5F)
                    .build()
    );
}
