package com.github.imdmk.doublejump.jump.feature.item.configuration;

import com.github.imdmk.doublejump.jump.feature.item.JumpItem;
import com.github.imdmk.doublejump.jump.feature.velocity.JumpVelocity;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JumpItemSerializer implements ObjectSerializer<JumpItem> {

    @Override
    public boolean supports(@NotNull Class<? super JumpItem> type) {
        return JumpItem.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NotNull JumpItem item, @NotNull SerializationData data, @NotNull GenericsDeclaration generics) {
        data.add("material", item.material(), Material.class);
        data.add("name", item.name(), Component.class);

        data.add("jumpProperties", item.jumpVelocity(), JumpVelocity.class);

        if (!item.lore().isEmpty()) {
            data.addCollection("lore", item.lore(), Component.class);
        }

        if (!item.flags().isEmpty()) {
            data.addCollection("flags", item.flags(), ItemFlag.class);
        }

        if (!item.enchantments().isEmpty()) {
            data.addAsMap("enchantments", item.enchantments(), Enchantment.class, Integer.class);
        }
    }

    @Override
    public JumpItem deserialize(@NotNull DeserializationData data, @NotNull GenericsDeclaration generics) {
        Material material = data.get("material", Material.class);
        Component name = data.get("name", Component.class);
        List<Component> lore = data.getAsList("lore", Component.class);

        JumpVelocity jumpVelocity = data.get("jumpProperties", JumpVelocity.class);

        List<ItemFlag> flags = data.containsKey("flags") ?
                data.getAsList("flags", ItemFlag.class) : Collections.emptyList();

        Map<Enchantment, Integer> enchantments = data.containsKey("enchantments") ?
                data.getAsMap("enchantments", Enchantment.class, Integer.class) : new HashMap<>();

        return JumpItem.builder()
                .material(material)
                .nameComponent(name)
                .loreComponent(lore)
                .jumpProperties(jumpVelocity)
                .itemFlags(flags)
                .enchantment(enchantments)
                .build();
    }
}
