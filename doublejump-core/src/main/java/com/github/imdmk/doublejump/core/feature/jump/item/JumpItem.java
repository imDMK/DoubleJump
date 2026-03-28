package com.github.imdmk.doublejump.core.feature.jump.item;

import com.github.imdmk.doublejump.core.feature.jump.velocity.JumpVelocity;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public record JumpItem(
        Material material,
        Component name,
        List<Component> lore,
        JumpVelocity velocity,
        List<ItemFlag> flags,
        Map<Enchantment, Integer> enchantments
) {

    public ItemStack toItemStack() {
        return ItemBuilder.from(material)
                .name(name)
                .lore(lore == null ? List.of() : lore)
                .enchant(enchantments == null ? Map.of() : enchantments)
                .flags((flags == null ? List.<ItemFlag>of() : flags).toArray(new ItemFlag[0]))
                .build();
    }
}
