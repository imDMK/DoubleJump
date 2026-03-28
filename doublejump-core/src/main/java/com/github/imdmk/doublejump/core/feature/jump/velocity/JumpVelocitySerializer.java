package com.github.imdmk.doublejump.core.feature.jump.velocity;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.jetbrains.annotations.NotNull;

public final class JumpVelocitySerializer implements ObjectSerializer<JumpVelocity> {

    @Override
    public boolean supports(@NotNull Class<? super JumpVelocity> type) {
        return JumpVelocity.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(
            @NotNull JumpVelocity velocity,
            @NotNull SerializationData data,
            @NotNull GenericsDeclaration generics
    ) {
        data.add("horizontal", velocity.horizontalBoost(), double.class);
        data.add("vertical", velocity.verticalBoost(), double.class);
    }

    @Override
    public JumpVelocity deserialize(
            @NotNull DeserializationData data,
            @NotNull GenericsDeclaration generics
    ) {
        double horizontal = data.get("horizontal", double.class);
        double vertical = data.get("vertical", double.class);
        return JumpVelocity.of(horizontal, vertical);
    }
}
