package com.github.imdmk.doublejump.jump.feature.visual.particle.gui;

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

public class JumpParticleGuiConfiguration extends OkaeriConfig {

    @Comment("# Title of the jump particle selection GUI")
    public Component title = ComponentUtil.text("<red>Choose jump particles");

    @Comment("# Item used to disable particle effects")
    public ConfigGuiItem disableItem = ConfigGuiItem.builder()
            .material(Material.BARRIER)
            .name("<red>DISABLE PARTICLES")
            .lore(
                    " ",
                    "<red>Choose this to disable jump particles.",
                    " "
            )
            .flags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES)
            .build();

    @Comment("# Sound played when the player clicks the disable item.")
    public JumpSound disableItemClickSound = new JumpSound(Sound.ENTITY_VILLAGER_NO, 0.5F, 0.5F);

    @Comment("# Prefix color used to display the name of the selected particle.")
    public String nameColor = "";

    @Comment("# Lore shown on the currently selected particle option.")
    public List<Component> activeLore = ComponentUtil.notItalic(
            " ",
            "<green>Selected",
            "<gray>Click <green>RIGHT <gray>to <red>remove this jump particle.",
            " "
    );

    @Comment("# Lore shown on a non-selected particle option.")
    public List<Component> defaultLore = ComponentUtil.notItalic(
            " ",
            "<gray>Click <green>RIGHT <gray>to <green>add this jump particle.",
            " "
    );

    @Comment("# Sound played when the player clicks to enable particle.")
    public JumpSound enableParticleClickSound = new JumpSound(Sound.ENTITY_VILLAGER_YES, 0.5F, 0.5F);
}
