package com.github.imdmk.doublejump.jump.item;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class JumpItemConfiguration extends OkaeriConfig {

    @Comment({
            "# Required item to use double jump",
            "# Determines if the player can receive and use double jump boots"
    })
    public boolean jumpItemEnabled = false;

    @Comment({
            "# Decrease the durability of an item after using it",
            "# Set to 0 to disable"
    })
    public int reduceJumpItemDurability = 0;

    @Comment({
            "# Cancel item usage?",
            "# Useful when you want the player to be unable to wear, for example, boots"
    })
    public boolean cancelJumpItemUse = true;
    @Comment("# Use a double jump after using a double jump item?")
    public boolean jumpItemUseDoubleJump = true;

    @Comment("# Remove jump item after player use jump item?")
    public boolean removeJumpItemAfterUse = true;
    @Comment("# Disable double jump mode after player use jump item?")
    public boolean disableJumpModeAfterUse = true;
    @Comment({
            "# Toggle double jump mode after player uses jump item?",
            "# The player can decide if he wants double jump mode"
    })
    public boolean jumpItemUseSwitchDoubleJump = false;

    @Comment("# Disable double jump mode when player drops jump item?")
    public boolean jumpItemDropCancelJumpMode = false;
    @Comment("# Delete jump item when player drops this item?")
    public boolean jumpItemDropDelete = false;
    @Comment("# Cancel drop item when player tries to drop jump item?")
    public boolean jumpItemDropCancel = false;

    @Comment({
            "# Cancel jump item repair in anvil?",
            "# If you allow it, the player will be able to repair the item, but cannot rename it (Renaming will make the item inoperable)."
    })
    public boolean jumpItemCancelRepair = false;
    @Comment({
            "# Cancel enchant jump item in enchanting?",
            "# If you disable this option, the plugin will start ignoring enchants on items",
    })
    public boolean jumpItemCancelEnchant = true;

    @Comment({
            "# This specifies the use of a double jump",
            "# Available usages:",
            "# HAVE_ITEM - The player must have the jump item in inventory",
            "# HOLD_ITEM - The player must hold the jump item",
            "# CLICK_ITEM - The player must right click item",
            "# WEAR_ITEM - The player must wear item",
    })
    public JumpItemUsage jumpItemUsage = JumpItemUsage.CLICK_ITEM;

    public ItemStack jumpItem = ItemBuilder.from(Material.DIAMOND_BOOTS)
            .name(Component.text("<red>DOUBLE JUMP"))
            .lore(Component.empty(), Component.text("<red>This is double jump item!"), Component.empty())
            .flags(ItemFlag.HIDE_ATTRIBUTES)
            .enchant(Enchantment.DURABILITY, 10)
            .build();
}
