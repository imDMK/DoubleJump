package com.github.imdmk.doublejump.jump.feature.visual.gui;

import com.github.imdmk.doublejump.infrastructure.gui.configuration.ConfigGuiItem;
import com.github.imdmk.doublejump.jump.feature.visual.particle.gui.JumpParticleGuiConfiguration;
import com.github.imdmk.doublejump.jump.feature.visual.sound.gui.JumpSoundGuiConfiguration;
import com.github.imdmk.doublejump.util.ComponentUtil;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

public class JumpVisualGuiConfiguration extends OkaeriConfig {

    @Comment("# Enable GUI visual settings?")
    public boolean enabled = true;

    @Comment("# GUI title for double jump visual settings")
    public Component title = ComponentUtil.text("<red>Double jump visual settings");

    @Comment("# Button to open the particle settings menu")
    public ConfigGuiItem particleItem = ConfigGuiItem.builder()
            .material(Material.NETHER_STAR)
            .name("<rainbow>Particles")
            .lore(
                    " ",
                    " <green>Click <gray>to open <rainbow>particle settings",
                    " "
            )
            .slot(12)
            .flags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES)
            .enchantment(Enchantment.LOYALTY, 1)
            .build();

    @Comment("# Button to open the sound settings menu")
    public ConfigGuiItem soundItem = ConfigGuiItem.builder()
            .material(Material.NOTE_BLOCK)
            .name("<rainbow>Sound")
            .lore(
                    " ",
                    " <green>Click <gray>to open <rainbow>sound settings",
                    " "
            )
            .slot(14)
            .flags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES)
            .enchantment(Enchantment.LOYALTY, 1)
            .build();

    @Comment("# Configuration for the particle settings GUI")
    public JumpParticleGuiConfiguration particleGui = new JumpParticleGuiConfiguration();

    @Comment("# Configuration for the sound settings GUI")
    public JumpSoundGuiConfiguration soundGui = new JumpSoundGuiConfiguration();
}
