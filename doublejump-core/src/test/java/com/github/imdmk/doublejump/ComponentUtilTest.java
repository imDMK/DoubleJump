package com.github.imdmk.doublejump;

import com.github.imdmk.doublejump.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComponentUtilTest {

    @Test
    void testSerialize() {
        Component toSerialize = Component.text("DMK").color(NamedTextColor.RED);

        String excepted = "<red>DMK";
        String result = ComponentUtil.serialize(toSerialize);

        assertEquals(excepted, result);
    }

    @Test
    void testDeserialize() {
        String resultContent = "<red>DMK";

        Component excepted = Component.text("DMK").color(NamedTextColor.RED);
        Component result = ComponentUtil.deserialize(resultContent);

        assertEquals(excepted, result);
    }

    @Test
    void testLegacyDeserialize() {
        String resultContent = "§4DMK";

        Component excepted = Component.text("DMK").color(NamedTextColor.DARK_RED);
        Component result = ComponentUtil.deserialize(resultContent);

        assertEquals(excepted, result);
    }
}