package com.github.imdmk.doublejump.core.shared.permission;

import org.bukkit.entity.Player;

import java.util.Map;

public final class PermissionBasedValueProvider<T>
        implements PlayerValueProvider<T> {

    private final T defaultValue;
    private final Map<String, T> permissionValues;

    public PermissionBasedValueProvider(PermissionValueConfig<T> config) {
        this.defaultValue = config.defaultValue();
        this.permissionValues = config.permissionValues();
    }

    @Override
    public T resolve(Player player) {
        for (Map.Entry<String, T> entry : permissionValues.entrySet()) {
            if (player.hasPermission(entry.getKey())) {
                return entry.getValue();
            }
        }

        return defaultValue;
    }
}