package com.github.imdmk.doublejump.jump.feature.item;

import com.github.imdmk.doublejump.jump.JumpConfiguration;
import com.github.imdmk.doublejump.jump.feature.item.usage.ItemUsage;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.Objects;

/**
 * Service responsible for handling logic related to jump items.
 * <p>
 * This includes verifying if an item is a valid jump item,
 * checking usage configuration, and retrieving the configured item.
 */
public class JumpItemService {

    @Inject private JumpConfiguration jumpConfiguration;

    /**
     * Checks whether the provided item matches the configured jump item.
     * <p>
     * This method compares metadata such as damage and enchantments
     * to ensure the item is functionally equivalent to the configured jump item.
     *
     * @param compare the item to compare with the configured jump item
     * @return {@code true} if the item is considered a jump item, {@code false} otherwise
     */
    public boolean isJumpItem(@Nullable ItemStack compare) {
        if (compare == null || !this.isEnabled()) {
            return false;
        }

        ItemStack jumpItem = this.getJumpItem().asItemStack();
        if (jumpItem.getType() != compare.getType()) {
            return false;
        }

        ItemMeta jumpMeta = jumpItem.getItemMeta();
        ItemMeta compareMeta = compare.getItemMeta();

        if (jumpMeta == null && compareMeta == null) {
            return true;
        }

        if (jumpMeta == null || compareMeta == null) {
            return false;
        }

        // IGNORE DURABILITY
        if (jumpMeta instanceof Damageable jumpDamageable &&
                compareMeta instanceof Damageable compareDamageable) {
            compareDamageable.setDamage(jumpDamageable.getDamage());
        }

        // ENCHANTS
        if (!jumpMeta.getEnchants().equals(compareMeta.getEnchants())) {
            return false;
        }

        // NAME
        if (jumpMeta.hasDisplayName() || compareMeta.hasDisplayName()) {
            if (!Objects.equals(jumpMeta.getDisplayName(), compareMeta.getDisplayName())) {
                return false;
            }
        }

        // LORE
        if (jumpMeta.hasLore() || compareMeta.hasLore()) {
            if (!Objects.equals(jumpMeta.getLore(), compareMeta.getLore())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Retrieves the configured jump item.
     *
     * @return the jump {@link ItemStack}
     */
    public JumpItem getJumpItem() {
        return this.jumpConfiguration.jumpItem.item;
    }

    /**
     * Checks whether the jump item feature is enabled.
     *
     * @return {@code true} if enabled, {@code false} otherwise
     */
    public boolean isEnabled() {
        return this.jumpConfiguration.jumpItem.enabled;
    }

    /**
     * Checks whether the jump item feature is enabled for a specific usage type.
     *
     * @param usage the usage mode to check
     * @return {@code true} if enabled and matches the given usage mode
     */
    public boolean isEnabled(@NotNull ItemUsage usage) {
        return this.isEnabled() && this.jumpConfiguration.jumpItem.usageMode.equals(usage);
    }
}
