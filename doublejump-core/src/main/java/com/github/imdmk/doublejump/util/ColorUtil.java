package com.github.imdmk.doublejump.util;

import org.bukkit.Color;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ColorUtil {

    private static final Map<String, Color> colors = new HashMap<>();

    private ColorUtil() {
        throw new UnsupportedOperationException("This is utility class.");
    }

    static {
        for (Field field : Color.class.getFields()) {
            if (!field.getType().equals(Color.class)) {
                continue;
            }

            try {
                String colorName = field.getName();
                Color color = (Color) field.get(Color.class);

                colors.put(colorName, color);
            }
            catch (IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    public static Optional<Color> get(String colorName) {
        return Optional.ofNullable(colors.get(colorName));
    }

    public static Optional<String> getName(Color color) {
        return colors.entrySet().stream()
                .filter(entry -> entry.getValue().equals(color))
                .map(Map.Entry::getKey)
                .findFirst();
    }
}
