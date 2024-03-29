package com.github.imdmk.doublejump.configuration.serializer;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class ItemStackSerializer implements ObjectSerializer<ItemStack> {

    @Override
    public boolean supports(@NonNull Class<? super ItemStack> type) {
        return ItemStack.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull ItemStack itemStack, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("material", itemStack.getType(), Material.class);

        if (itemStack.getAmount() > 1) {
            data.add("amount", itemStack.getAmount(), Integer.class);
        }

        if (itemStack instanceof Damageable damageable) {
            if (damageable.getDamage() != 0) {
                data.add("durability", damageable.getDamage(), Integer.class);
            }
        }

        if (itemStack.hasItemMeta()) {
            data.add("item-meta", itemStack.getItemMeta(), ItemMeta.class);
        }
    }

    @Override
    public ItemStack deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        Material material = data.get("material", Material.class);

        int amount = data.containsKey("amount")
                ? data.get("amount", Integer.class) : 1;

        int durability = data.containsKey("durability")
                ? data.get("durability", Integer.class) : 0;

        Optional<ItemMeta> itemMetaOptional = Optional.ofNullable(
                data.get("item-meta", ItemMeta.class)
        );

        ItemStack itemStack = new ItemStack(material, amount);

        itemMetaOptional.ifPresent(itemStack::setItemMeta);

        if (itemStack.getItemMeta() instanceof Damageable damageable) {
            damageable.setDamage(durability);
            itemStack.setItemMeta(damageable);
        }

        return itemStack;
    }
}
