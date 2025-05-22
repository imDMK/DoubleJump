package com.github.imdmk.doublejump.configuration.serializer;

import com.github.imdmk.doublejump.util.ColorUtil;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;

public class ColorSerializer implements ObjectSerializer<Color> {

    @Override
    public boolean supports(@NotNull Class<? super Color> type) {
        return Color.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NotNull Color color, @NotNull SerializationData data, @NotNull GenericsDeclaration generics) {
        data.setValue(ColorUtil.getName(color), String.class);
    }

    @Override
    public Color deserialize(@NotNull DeserializationData data, @NotNull GenericsDeclaration generics) {
        return ColorUtil.get(data.getValue(String.class));
    }
}
