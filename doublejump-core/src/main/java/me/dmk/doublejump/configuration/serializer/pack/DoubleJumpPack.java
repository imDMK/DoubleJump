package me.dmk.doublejump.configuration.serializer.pack;

import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.SerdesRegistry;
import me.dmk.doublejump.configuration.serializer.ComponentSerializer;
import me.dmk.doublejump.configuration.serializer.EnchantmentSerializer;
import me.dmk.doublejump.configuration.serializer.ItemMetaSerializer;
import me.dmk.doublejump.configuration.serializer.ItemStackSerializer;
import me.dmk.doublejump.configuration.serializer.NotificationSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class DoubleJumpPack implements OkaeriSerdesPack {

    @Override
    public void register(@NonNull SerdesRegistry registry) {
        registry.register(new ComponentSerializer());
        registry.register(new EnchantmentSerializer());
        registry.register(new ItemStackSerializer());
        registry.register(new ItemMetaSerializer());
        registry.register(new NotificationSerializer());
    }
}
