package com.github.imdmk.doublejump.jump.item.configuration;

import com.github.imdmk.doublejump.jump.item.JumpItemUsage;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class JumpItemSettings extends OkaeriConfig {

    @Comment("# Specifies whether the item should be enabled")
    public boolean enabled = false;

    @Comment({
            "# Cancel jump item repair in anvil?",
            "# If you allow it, the player will be able to repair the item, but will not be able to rename it (renaming will make the item inoperable)"
    })
    public boolean cancelRepair = false;

    @Comment({
            "# Cancel enchant jump item in enchanting?",
            "# If you disable this option, the plugin will start ignoring enchantments on items when checking if a player has a jump item",
    })
    public boolean cancelEnchant = true;

    @Comment("# Jump item")
    public ItemStack item = ItemBuilder.from(Material.DIAMOND_BOOTS)
            .name(Component.text("<red>DOUBLE JUMP"))
            .lore(Component.empty(), Component.text("<red>This is double jump item!"), Component.empty())
            .flags(ItemFlag.HIDE_ATTRIBUTES)
            .enchant(Enchantment.DURABILITY, 10)
            .build();

    @Comment({
            "# ",
            "# Jump item usage configuration",
            "# "
    })
    public UsageConfiguration usageConfiguration = new UsageConfiguration();

    public static class UsageConfiguration extends OkaeriConfig {

        @Comment({
                "# This specifies the use of a double jump",
                "# Available usages:",
                "# HAVE_ITEM - The player must have the jump item in inventory",
                "# HOLD_ITEM - The player must hold the jump item",
                "# CLICK_ITEM - The player must right click item",
                "# WEAR_ITEM - The player must wear item",
        })
        public JumpItemUsage usage = JumpItemUsage.CLICK_ITEM;

        @Comment("# Specifies whether to cancel the use of the item")
        public boolean cancel = false;

        @Comment("# Specifies whether to remove an item after using it")
        public boolean delete = false;

        @Comment({
                "# Specifies whether to reduce the durability of an item after using it",
                "# Example: ",
                "# reduceDurability: 5",
                "# The durability of the item will be reduced by 5",
                "# To disable, set the value to 0"
        })
        public int reduceDurability = 0;

        @Comment("# Specifies whether to double jump after using a jump item")
        public boolean doubleJump = false;

        @Comment("# Specifies whether to cancel double jump mode after using a jump item")
        public boolean disableDoubleJumpMode = false;

        @Comment("# Specifies whether to toggle double jump mode after using a jump item")
        public boolean switchDoubleJumpMode = false;

    }

    @Comment({
            "# ",
            "# Jump item drop configuration",
            "# "
    })
    public DropConfiguration dropConfiguration = new DropConfiguration();

    public static class DropConfiguration extends OkaeriConfig {

        @Comment("# Specifies whether to cancel the item drop")
        public boolean cancel = false;

        @Comment("# Specifies whether to remove the item after dropping it")
        public boolean delete = false;

        @Comment("# Specifies whether to cancel double jump mode after dropping an item")
        public boolean disableDoubleJumpMode = false;

    }
}
