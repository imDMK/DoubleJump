package com.github.imdmk.doublejump.core.config.serializer;

import com.github.imdmk.doublejump.core.platform.adventure.AdventureComponents;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public final class ComponentSerializer implements ObjectSerializer<Component> {

    @Override
    public boolean supports(@NotNull Class<? super Component> type) {
        return Component.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(
            @NotNull Component component,
            @NotNull SerializationData data,
            @NotNull GenericsDeclaration generics
    ) {
        data.setValue(AdventureComponents.serialize(component), String.class);
    }

    @Override
    public Component deserialize(
            @NotNull DeserializationData data,
            @NotNull GenericsDeclaration generics
    ) {
        return AdventureComponents.text(data.getValue(String.class));
    }
}
