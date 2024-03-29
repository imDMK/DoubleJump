package com.github.imdmk.doublejump.configuration.transformer;

import com.github.imdmk.doublejump.util.ColorUtil;
import eu.okaeri.configs.schema.GenericsPair;
import eu.okaeri.configs.serdes.BidirectionalTransformer;
import eu.okaeri.configs.serdes.SerdesContext;
import org.bukkit.Color;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class ColorTransformer extends BidirectionalTransformer<Color, String> {

    @Override
    public GenericsPair<Color, String> getPair() {
        return this.genericsPair(Color.class, String.class);
    }

    @Override
    public String leftToRight(@NonNull Color color, @NonNull SerdesContext serdesContext) {
        Optional<String> colorNameOptional = ColorUtil.getName(color);

        return colorNameOptional.orElseThrow(() -> new IllegalStateException("Unknown color: " + color));
    }

    @Override
    public Color rightToLeft(@NonNull String colorName, @NonNull SerdesContext serdesContext) {
        Optional<Color> colorOptional = ColorUtil.get(colorName);

        return colorOptional.orElseThrow(() -> new IllegalStateException("Unknown color name: " + colorName));
    }
}
