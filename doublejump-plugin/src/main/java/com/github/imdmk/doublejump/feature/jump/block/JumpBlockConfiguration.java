package com.github.imdmk.doublejump.feature.jump.block;

import com.github.imdmk.doublejump.jump.JumpVelocity;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class JumpBlockConfiguration extends OkaeriConfig {

    @Comment("# Determines whether block-based double jump is enabled")
    public boolean enabled = true;

    @Comment({
            "# List of configured jump blocks",
            "# Each block defines a material and custom jump velocity"
    })
    public List<JumpBlock> blocks = List.of(
            new JumpBlock(Material.DIAMOND_BLOCK, JumpVelocity.of(0.9, 0.6)),
            new JumpBlock(Material.GOLD_BLOCK, JumpVelocity.of(0.3, 0.3))
    );

    /**
     * Finds a jump block configuration by its material.
     *
     * @param material the block material to search for
     * @return optional jump block configuration
     */
    public Optional<JumpBlock> getJumpBlock(@NotNull Material material) {
        return this.blocks.stream()
                .filter(jumpBlock -> jumpBlock.type().equals(material))
                .findAny();
    }
}
