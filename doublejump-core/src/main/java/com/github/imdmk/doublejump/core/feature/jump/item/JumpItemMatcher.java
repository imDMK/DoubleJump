package com.github.imdmk.doublejump.core.feature.jump.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public final class JumpItemMatcher {

    private final ItemStack referenceItem;

    public JumpItemMatcher(JumpItem jumpItem) {
        this.referenceItem = jumpItem.toItemStack();
    }

    public boolean matches(ItemStack comparedItem) {
        if (comparedItem == null) {
            return false;
        }

        if (referenceItem.getType() != comparedItem.getType()) {
            return false;
        }

        ItemMeta referenceMeta = referenceItem.getItemMeta();
        ItemMeta comparedMeta = comparedItem.getItemMeta();
        if (referenceMeta == null || comparedMeta == null) {
            return referenceMeta == comparedMeta;
        }

        return compareMeta(referenceMeta, comparedMeta);
    }

    private boolean compareMeta(ItemMeta reference, ItemMeta compared) {
        if (!Objects.equals(reference.getDisplayName(), compared.getDisplayName())) {
            return false;
        }

        if (!Objects.equals(reference.getLore(), compared.getLore())) {
            return false;
        }

        if (!reference.getEnchants().equals(compared.getEnchants())) {
            return false;
        }

        return true;
    }
}