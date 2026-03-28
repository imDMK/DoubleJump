package com.github.imdmk.doublejump.core.shared.permission;

import java.util.Map;

public record PermissionValueConfig<T>(T defaultValue, Map<String, T> permissionValues) {
}
