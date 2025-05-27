package com.github.imdmk.doublejump.jump.feature.visual.sound.gui;

import com.github.imdmk.doublejump.infrastructure.gui.configuration.item.ConfigGuiItem;
import com.github.imdmk.doublejump.jump.feature.visual.sound.JumpSound;
import com.github.imdmk.doublejump.util.ComponentUtil;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;

import java.util.List;

public class JumpSoundGuiConfiguration extends OkaeriConfig {

    @Comment("# Title of the jump sound selection GUI")
    public Component title = ComponentUtil.text("<red>Choose new jump sound");

    @Comment("# Item used to disable jump sound")
    public ConfigGuiItem disableItem = ConfigGuiItem.builder()
            .material(Material.BARRIER)
            .name("<red>DISABLE SOUND")
            .lore(
                    " ",
                    "<red>Choose this to disable jump sound.",
                    " "
            )
            .flags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES)
            .build();

    @Comment("# Sound played when the player clicks the disable sound item.")
    public JumpSound disableItemClickSound = new JumpSound(Sound.ENTITY_VILLAGER_NO, 0.5F, 0.5F);

    @Comment("# Prefix color used to display the name of the selected sound.")
    public String nameColor = "";

    @Comment("# Lore shown on the selected (active) sound option.")
    public List<Component> activeLore = ComponentUtil.notItalic(
            " ",
            "<green>Selected",
            "<gray>Click <green>LEFT <gray>to <green>preview this sound.",
            " "
    );

    @Comment("# Lore shown on a non-selected (default) sound option.")
    public List<Component> defaultLore = ComponentUtil.notItalic(
            " ",
            "<gray>Click <yellow>LEFT <gray>to <yellow>preview <gray>this sound.",
            "<gray>Click <green>RIGHT <gray>to <green>choose <gray>this sound.",
            " "
    );
}
