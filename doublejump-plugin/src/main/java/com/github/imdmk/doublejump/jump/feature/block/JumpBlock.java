package com.github.imdmk.doublejump.jump.feature.block;

import com.github.imdmk.doublejump.jump.feature.velocity.JumpVelocity;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a block that grants a double jump boost when a player stands on it.
 *
 * @param type the material type of the block
 * @param jumpVelocity the custom jump velocity applied when jumping from this block
 */
public record JumpBlock(@NotNull Material type, @NotNull JumpVelocity jumpVelocity) {
}
