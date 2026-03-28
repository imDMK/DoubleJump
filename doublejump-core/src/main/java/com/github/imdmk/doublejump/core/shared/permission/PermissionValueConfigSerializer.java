package com.github.imdmk.doublejump.core.shared.permission;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public final class PermissionValueConfigSerializer<T>
        implements ObjectSerializer<PermissionValueConfig<T>> {

    @Override
    public boolean supports(@NotNull Class<? super PermissionValueConfig<T>> type) {
        return PermissionValueConfig.class.isAssignableFrom(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void serialize(
            @NotNull PermissionValueConfig<T> value,
            @NotNull SerializationData data,
            @NotNull GenericsDeclaration generics
    ) {
        Class<T> type = (Class<T>) generics.getSubtypeAtOrThrow(0).getType();

        data.add("base", value.defaultValue(), type);
        data.addAsMap("overrides", value.permissionValues(), String.class, type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PermissionValueConfig<T> deserialize(
            @NotNull DeserializationData data,
            @NotNull GenericsDeclaration generics
    ) {
        Class<T> type = (Class<T>) generics.getSubtypeAtOrThrow(0).getType();

        T defaultValue = data.get("base", type);

        Map<String, T> permissionValues;
        if (data.containsKey("overrides")) {
            if (List.class.isAssignableFrom(type)) {
                permissionValues = (Map<String, T>) data.get("overrides", Map.class);
            }
            else {
                permissionValues = data.getAsMap("overrides", String.class, type);
            }

        } else {
            permissionValues = Map.of();
        }

        return new PermissionValueConfig<>(defaultValue, permissionValues);
    }
}