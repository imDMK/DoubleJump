package com.github.imdmk.doublejump.jump.feature.block;

import com.github.imdmk.doublejump.jump.feature.velocity.JumpVelocity;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class JumpBlockSerializer implements ObjectSerializer<JumpBlock> {

    @Override
    public boolean supports(@NotNull Class<? super JumpBlock> type) {
        return JumpBlock.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NotNull JumpBlock block, @NotNull SerializationData data, @NotNull GenericsDeclaration generics) {
        data.add("material", block.type(), Material.class);
        data.add("jumpProperties", block.jumpVelocity(), JumpVelocity.class);
    }

    @Override
    public JumpBlock deserialize(@NotNull DeserializationData data, @NotNull GenericsDeclaration generics) {
        Material material = data.get("material", Material.class);
        JumpVelocity jumpVelocity = data.get("jumpProperties", JumpVelocity.class);
        return new JumpBlock(material, jumpVelocity);
    }
}
