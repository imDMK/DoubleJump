package com.github.imdmk.doublejump.configuration.transformer;

import eu.okaeri.configs.schema.GenericsPair;
import eu.okaeri.configs.serdes.BidirectionalTransformer;
import eu.okaeri.configs.serdes.SerdesContext;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.checkerframework.checker.nullness.qual.NonNull;

public class EnchantmentTransformer extends BidirectionalTransformer<String, Enchantment> {

    @Override
    public GenericsPair<String, Enchantment> getPair() {
        return this.genericsPair(String.class, Enchantment.class);
    }

    @Override
    public Enchantment leftToRight(@NonNull String key, @NonNull SerdesContext serdesContext) {
        return Registry.ENCHANTMENT.get(NamespacedKey.minecraft(key));
    }

    @Override
    public String rightToLeft(@NonNull Enchantment enchantment, @NonNull SerdesContext serdesContext) {
        return enchantment.getKey().getKey();
    }
}
