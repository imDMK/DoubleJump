package com.github.imdmk.doublejump.core.feature.jump.item.config;

import com.github.imdmk.doublejump.core.feature.jump.item.JumpItem;
import com.github.imdmk.doublejump.core.feature.jump.velocity.JumpVelocity;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

final class JumpItemSerializer implements ObjectSerializer<JumpItem> {

    @Override
    public boolean supports(@NotNull Class<? super JumpItem> type) {
        return JumpItem.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(
            @NotNull JumpItem item,
            @NotNull SerializationData data,
            @NotNull GenericsDeclaration generics
    ) {
        data.add("material", item.material(), Material.class);
        data.add("name", item.name(), Component.class);
        data.addCollection("lore", item.lore(), Component.class);
        data.add("velocity", item.velocity(), JumpVelocity.class);
        data.addCollection("flags", item.flags(), ItemFlag.class);
        data.addAsMap("enchantments", item.enchantments(), Enchantment.class, Integer.class);
    }

    @Override
    public JumpItem deserialize(
            @NotNull DeserializationData data,
            @NotNull GenericsDeclaration generics
    ) {
        Material material = data.get("material", Material.class);
        Component name = data.get("name", Component.class);
        List<Component> lore = data.getAsList("lore", Component.class);
        JumpVelocity velocity = data.get("velocity", JumpVelocity.class);
        List<ItemFlag> flags = data.getAsList("flags", ItemFlag.class);
        Map<Enchantment, Integer> enchantments = data.getAsMap("enchantments", Enchantment.class, Integer.class);

        return new JumpItem(
                material,
                name,
                lore,
                velocity,
                flags,
                enchantments
        );
    }
}
