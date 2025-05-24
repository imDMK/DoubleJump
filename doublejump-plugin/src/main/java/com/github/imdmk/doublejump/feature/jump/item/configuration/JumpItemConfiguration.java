package com.github.imdmk.doublejump.feature.jump.item.configuration;

import com.github.imdmk.doublejump.feature.jump.item.JumpItem;
import com.github.imdmk.doublejump.feature.jump.item.usage.ItemUsage;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.util.List;

public class JumpItemConfiguration extends OkaeriConfig {

    @Comment("# Enables or disables the jump item feature.")
    public boolean enabled = true;

    @Comment("# Prevent repairing the jump item using anvils?")
    public boolean preventRepair = true;

    @Comment("# Prevent dropping the jump item from the player's inventory?")
    public boolean preventDrop = false;

    @Comment("# Prevent enchanting the jump item?")
    public boolean preventEnchant = true;

    @Comment({
            "# Defines how the jump item must be used to activate double jump.",
            "# Available modes:",
            "#  - WEAR_ITEM:     The item must be worn (e.g. boots in armor slot).",
            "#  - HOLD_ITEM:     The item must be held in main or left hand.",
            "#  - CLICK_ITEM:    The item must be right-clicked.",
            "#  - HAVE_ITEM:     The item just needs to be in the player's inventory.",
    })
    public ItemUsage usageMode = ItemUsage.WEAR_ITEM;

    @Comment("# The actual item given or used to enable the double jump.")
    public JumpItem item = JumpItem.builder()
            .material(Material.DIAMOND_BOOTS)
            .name("<rainbow>DOUBLE JUMP")
            .lore(
                    " ",
                    " <rainbow>WEAR THIS ITEM and you see magic",
                    " <dark_gray>configure jump item usage in config",
                    " "
            )
            .itemFlags(List.of(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES))
            .enchantment(Enchantment.LOYALTY, 3)
            .build();

    @Comment({
            "# Specifies how much durability to reduce after a jump.",
            "# Example:",
            "# reduceDurability: 5",
            "# The item's durability will be reduced by 5 on use.",
            "# Set to 0 to disable durability reduction."
    })
    public int reduceDurability = 5;

}
