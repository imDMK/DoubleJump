package com.github.imdmk.doublejump.feature.jump.item;

import com.github.imdmk.doublejump.jump.JumpVelocity;
import com.github.imdmk.doublejump.util.ComponentUtil;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record JumpItem(@NotNull Material material,
                       @NotNull Component name, List<Component> lore,
                       @NotNull JumpVelocity jumpVelocity,
                       List<ItemFlag> flags, Map<Enchantment, Integer> enchantments) {

    @Override
    public @NotNull Material material() {
        return this.material;
    }

    @Override
    public @NotNull Component name() {
        return this.name;
    }

    @Override
    public @NotNull List<Component> lore() {
        if (this.lore == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(this.lore);
    }

    public @NotNull JumpVelocity jumpVelocity() {
        return this.jumpVelocity;
    }

    @Override
    public @NotNull List<ItemFlag> flags() {
        if (this.flags == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(this.flags);
    }

    @Override
    public @NotNull Map<Enchantment, Integer> enchantments() {
        if (this.enchantments == null) {
            return Collections.emptyMap();
        }

        return Collections.unmodifiableMap(this.enchantments);
    }

    public @NotNull ItemStack asItemStack() {
        return ItemBuilder.from(this.material())
                .name(this.name())
                .lore(this.lore())
                .enchant(this.enchantments())
                .flags(this.flags().toArray(new ItemFlag[0]))
                .build();
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Material material;
        private Component name;
        private List<Component> lore;

        private JumpVelocity jumpVelocity;

        private List<ItemFlag> itemFlags;
        private Map<Enchantment, Integer> enchantments = new HashMap<>();

        private Builder() {}

        @Contract("_ -> this")
        public Builder material(@NotNull Material material) {
            this.material = material;
            return this;
        }

        @Contract("_ -> this")
        public Builder nameComponent(@NotNull Component name) {
            this.name = name;
            return this;
        }

        @Contract("_ -> this")
        public Builder name(@NotNull String name) {
            this.name = ComponentUtil.notItalic(name);
            return this;
        }

        @Contract("_ -> this")
        public Builder loreComponent(@NotNull List<Component> lore) {
            this.lore = lore;
            return this;
        }

        @Contract("_ -> this")
        public Builder lore(@NotNull List<String> lore) {
            this.lore = ComponentUtil.notItalic(lore);
            return this;
        }

        @Contract("_ -> this")
        public Builder lore(@NotNull String... lore) {
            this.lore = ComponentUtil.notItalic(lore);
            return this;
        }

        @Contract("_ -> this")
        public Builder jumpProperties(@NotNull JumpVelocity jumpVelocity) {
            this.jumpVelocity = jumpVelocity;
            return this;
        }

        @Contract("_ -> this")
        public Builder itemFlags(@NotNull List<ItemFlag> itemFlags) {
            this.itemFlags = itemFlags;
            return this;
        }

        @Contract("_ -> this")
        public Builder enchantment(@NotNull Map<Enchantment, Integer> enchantments) {
            this.enchantments = enchantments;
            return this;
        }

        @Contract("_,_ -> this")
        public Builder enchantment(@NotNull Enchantment enchantment, int level) {
            this.enchantments.put(enchantment, level);
            return this;
        }

        public JumpItem build() {
            return new JumpItem(this.material, this.name, this.lore, this.jumpVelocity, this.itemFlags, this.enchantments);
        }
    }
}
