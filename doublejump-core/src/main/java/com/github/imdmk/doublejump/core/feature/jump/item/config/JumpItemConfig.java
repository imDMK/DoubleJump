package com.github.imdmk.doublejump.core.feature.jump.item.config;

import com.github.imdmk.doublejump.core.config.ConfigSection;
import com.github.imdmk.doublejump.core.config.serializer.ComponentSerializer;
import com.github.imdmk.doublejump.core.config.serializer.EnchantmentSerializer;
import com.github.imdmk.doublejump.core.feature.jump.item.JumpItem;
import com.github.imdmk.doublejump.core.feature.jump.item.JumpItemUsage;
import com.github.imdmk.doublejump.core.feature.jump.velocity.JumpVelocity;
import com.github.imdmk.doublejump.core.feature.jump.velocity.JumpVelocitySerializer;
import com.github.imdmk.doublejump.core.injector.annotations.ConfigFile;
import com.github.imdmk.doublejump.core.platform.adventure.AdventureComponents;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.util.List;
import java.util.Map;

@ConfigFile
public final class JumpItemConfig extends ConfigSection {

    @Comment({"#", "# Enable or disable the double jump item system", "#"})
    public boolean enabled = true;

    @Comment({
            "#",
            "# How the item activates double jump",
            "# CLICK_ITEM - use (click) the item",
            "# HOLD_ITEM  - holding/wearing enables jump",
            "# WEAR_ITEM - wearing enables jump",
            "# HOLD_ITEM - holding enabled jump",
            "#"
    })
    public JumpItemUsage usage = JumpItemUsage.WEAR_ITEM;

    @Comment({
            "#",
            "# Main item configuration",
            "# Customize material, name, lore, velocity and more",
            "#"
    })
    public JumpItem item = new JumpItem(
            Material.DIAMOND_BOOTS,
            AdventureComponents.notItalic("<green>DOUBLEJUMP ITEM"),
            AdventureComponents.notItalic(
                    " ",
                    "<dark_gray>• <green>WEAR <gray>to activate double jump!",
                    " "
            ),
            JumpVelocity.of(0.9, 0.35),
            List.of(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES),
            Map.of(Enchantment.UNBREAKING, 3)
    );

    @Comment({
            "#",
            "# Prevent repairing the item (anvil, grindstone, etc.)",
            "#"
    })
    public boolean blockRepairing = true;

    @Comment({
            "#",
            "# Prevent dropping the item",
            "#"
    })
    public boolean blockDrop = false;

    @Comment({
            "#",
            "# Prevent enchanting the item",
            "#"
    })
    public boolean blockEnchanting = true;

    @Override
    public OkaeriSerdesPack serdesPack() {
        return registry -> {
            registry.register(new ComponentSerializer());
            registry.register(new EnchantmentSerializer());

            registry.register(new JumpItemSerializer());
            registry.register(new JumpVelocitySerializer());
        };
    }

    @Override
    public String fileName() {
        return "jumpItemConfig.yml";
    }
}
