package com.github.imdmk.doublejump.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for creating, manipulating, and serializing
 * Adventure {@link Component} instances using MiniMessage format.
 */
public final class ComponentUtil {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private ComponentUtil() {
        throw new UnsupportedOperationException("This is utility class.");
    }

    /**
     * Deserializes a MiniMessage string into a {@link Component}
     * and disables italic decoration.
     *
     * @param text MiniMessage formatted string
     * @return component with italic decoration disabled
     */
    public static Component notItalic(String text) {
        return MINI_MESSAGE.deserialize(text)
                .decoration(TextDecoration.ITALIC, false);
    }

    /**
     * Serializes a {@link Component} into its MiniMessage string representation.
     *
     * @param component the component to serialize
     * @return MiniMessage formatted string
     */
    public static String serialize(Component component) {
        return MINI_MESSAGE.serialize(component);
    }

    /**
     * Deserializes a MiniMessage string into a {@link Component}.
     *
     * @param text MiniMessage formatted string
     * @return deserialized component
     */
    public static Component text(String text) {
        return MINI_MESSAGE.deserialize(text);
    }

    /**
     * Deserializes multiple MiniMessage strings into a list of {@link Component}s.
     *
     * @param texts array of MiniMessage formatted strings
     * @return list of deserialized components
     */
    public static List<Component> text(String... texts) {
        List<Component> components = new ArrayList<>();
        for (String text : texts) {
            components.add(MINI_MESSAGE.deserialize(text));
        }
        return components;
    }

    /**
     * Deserializes a list of MiniMessage strings into a list of {@link Component}s.
     *
     * @param strings list of MiniMessage formatted strings
     * @return list of deserialized components
     */
    public static List<Component> text(List<String> strings) {
        return strings.stream()
                .map(ComponentUtil::text)
                .toList();
    }
}
