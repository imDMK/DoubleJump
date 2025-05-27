package com.github.imdmk.doublejump.jump.feature.velocity;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.jetbrains.annotations.NotNull;

public class JumpVelocitySerializer implements ObjectSerializer<JumpVelocity> {

    @Override
    public boolean supports(@NotNull Class<? super JumpVelocity> type) {
        return JumpVelocity.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NotNull JumpVelocity properties, @NotNull SerializationData data, @NotNull GenericsDeclaration generics) {
        data.add("horizontalBoost", properties.horizontalBoost(), double.class);
        data.add("verticalBoost", properties.verticalBoost(), double.class);
    }

    @Override
    public JumpVelocity deserialize(@NotNull DeserializationData data, @NotNull GenericsDeclaration generics) {
        double horizontalBoost = data.get("horizontalBoost", double.class);
        double verticalBoost = data.get("verticalBoost", double.class);
        return JumpVelocity.of(horizontalBoost, verticalBoost);
    }
}
