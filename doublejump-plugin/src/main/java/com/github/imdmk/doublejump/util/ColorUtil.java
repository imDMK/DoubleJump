package com.github.imdmk.doublejump.util;

import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for working with Bukkit {@link Color} constants by name.
 */
public final class ColorUtil {

    private static final Map<String, Color> colors;

    private ColorUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    static {
        Map<String, Color> temp = new HashMap<>();
        for (Field field : Color.class.getFields()) {
            if (!field.getType().equals(Color.class)) {
                continue;
            }

            try {
                String colorName = field.getName();
                Color color = (Color) field.get(Color.class);

                temp.put(colorName, color);
            }
            catch (IllegalAccessException exception) {
                throw new RuntimeException("Failed to access Color field " + field.getName(), exception);
            }
        }
        colors = Collections.unmodifiableMap(temp);
    }

    /**
     * Returns the {@link Color} for the given name (case-insensitive).
     *
     * @param colorName the name of the color; must not be null
     * @return the matching {@link Color}, or null if not found
     */
    public static @Nullable Color get(@NotNull String colorName) {
        return colors.get(colorName.toUpperCase());
    }

    /**
     * Returns the name of the given {@link Color}.
     *
     * @param color the color; must not be null
     * @return the name of the color constant
     * @throws IllegalArgumentException if the color is not found
     */
    public static @NotNull String getName(@NotNull Color color) {
        return colors.entrySet().stream()
                .filter(entry -> entry.getValue().equals(color))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not find color name for " + color));
    }
}
