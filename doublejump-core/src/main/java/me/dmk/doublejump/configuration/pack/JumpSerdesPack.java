package me.dmk.doublejump.configuration.pack;

import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.SerdesRegistry;
import me.dmk.doublejump.configuration.serializer.ComponentSerializer;
import me.dmk.doublejump.configuration.serializer.EnchantmentSerializer;
import me.dmk.doublejump.configuration.serializer.ItemMetaSerializer;
import me.dmk.doublejump.configuration.serializer.ItemStackSerializer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.checkerframework.checker.nullness.qual.NonNull;

public class JumpSerdesPack implements OkaeriSerdesPack {

    private final MiniMessage miniMessage;

    public JumpSerdesPack(MiniMessage miniMessage) {
        this.miniMessage = miniMessage;
    }

    @Override
    public void register(@NonNull SerdesRegistry registry) {
        registry.register(new ComponentSerializer(this.miniMessage));
        registry.register(new EnchantmentSerializer());
        registry.register(new ItemStackSerializer());
        registry.register(new ItemMetaSerializer(this.miniMessage));
    }
}
