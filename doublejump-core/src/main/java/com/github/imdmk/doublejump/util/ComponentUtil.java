package com.github.imdmk.doublejump.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;

public final class ComponentUtil {

    public static final CharSequence LEGACY_CHAR = "§";
    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private ComponentUtil() {
        throw new UnsupportedOperationException("This is utility class.");
    }

    public static String serialize(Component component) {
        return MINI_MESSAGE.serialize(component);
    }

    public static Component deserialize(String text) {
        return text.contains(LEGACY_CHAR)
                ? LegacyComponentSerializer.legacySection().deserialize(text)
                : MINI_MESSAGE.deserialize(text);
    }

    public static List<Component> deserialize(List<String> strings) {
        return strings.stream()
                .map(ComponentUtil::deserialize)
                .toList();
    }
}
