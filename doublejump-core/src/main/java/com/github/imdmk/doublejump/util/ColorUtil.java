package com.github.imdmk.doublejump.util;

import org.bukkit.Color;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ColorUtil {

    private static final Map<String, Color> colors = new ConcurrentHashMap<>();

    private ColorUtil() {
        throw new UnsupportedOperationException("This is utility class.");
    }

    static {
        for (Field field : Color.class.getFields()) {
            if (!field.getType().equals(Color.class)) {
                continue;
            }

            try {
                colors.put(field.getName(), (Color) field.get(Color.class));
            }
            catch (IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    public static Optional<Color> getColor(String colorName) {
        return Optional.ofNullable(colors.get(colorName));
    }

    public static Optional<String> getColorName(Color color) {
        return colors.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(color))
                .map(Map.Entry::getKey)
                .findFirst();
    }
}
