package com.github.imdmk.doublejump.feature.jump.particle;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class JumpParticleSerializer implements ObjectSerializer<JumpParticle> {

    @Override
    public boolean supports(@NotNull Class<? super JumpParticle> type) {
        return JumpParticle.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NotNull JumpParticle particle, @NotNull SerializationData data, @NotNull GenericsDeclaration generics) {
        data.add("particle", particle.particle(), Particle.class);
        data.add("count", particle.count(), Integer.class);

        data.add("color", particle.color(), Color.class);
        data.add("size", particle.size(), Integer.class);
        data.add("offsetX", particle.offsetX(), Double.class);
        data.add("offsetY", particle.offsetY(), Double.class);
        data.add("offsetZ", particle.offsetZ(), Double.class);
        data.add("extra", particle.extra(), Double.class);
    }

    @Override
    public JumpParticle deserialize(@NotNull DeserializationData data, @NotNull GenericsDeclaration generics) {
        Particle particle = data.get("particle", Particle.class);
        int count = data.get("count", Integer.class);

        Color color = data.get("color", Color.class);
        int size = data.get("size", Integer.class);
        double offsetX = data.get("offsetX", Double.class);
        double offsetY = data.get("offsetY", Double.class);
        double offsetZ = data.get("offsetZ", Double.class);
        double extra = data.get("extra", Double.class);

        return JumpParticle.builder(particle, count)
                .color(color)
                .size(size)
                .offsetX(offsetX)
                .offsetY(offsetY)
                .offsetZ(offsetZ)
                .extra(extra)
                .build();
    }
}
