package com.github.imdmk.doublejump.jump.feature.visual.sound.gui;

import com.github.imdmk.doublejump.jump.feature.visual.sound.JumpSound;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a mapping between a Minecraft sound effect and its corresponding GUI display item.
 *
 * @param jumpSound       The sound effect.
 * @param displayItem The item used to visually represent the sound in the GUI.
 */
public record JumpSoundEntry(@NotNull JumpSound jumpSound, @NotNull Material displayItem) {
}
