package com.github.imdmk.doublejump.jump.particle;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.checkerframework.checker.nullness.qual.NonNull;

public class JumpParticleSerializer implements ObjectSerializer<JumpParticle> {

    @Override
    public boolean supports(@NonNull Class<? super JumpParticle> type) {
        return JumpParticle.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull JumpParticle jumpParticle, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("particle", jumpParticle.particle(), Particle.class);
        data.add("color", jumpParticle.color(), Color.class);

        data.add("size", jumpParticle.size(), Integer.class);
        data.add("count", jumpParticle.count(), Integer.class);
        data.add("offsetX", jumpParticle.offsetX(), Double.class);
        data.add("offsetY", jumpParticle.offsetY(), Double.class);
        data.add("offsetZ", jumpParticle.offsetZ(), Double.class);
        data.add("extra", jumpParticle.extra(), Double.class);
    }

    @Override
    public JumpParticle deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        Particle particle = data.get("particle", Particle.class);
        Color color = data.get("color", Color.class);

        int size = data.get("size", Integer.class);
        int count = data.get("count", Integer.class);
        double offsetX = data.get("offsetX", Double.class);
        double offsetY = data.get("offsetY", Double.class);
        double offsetZ = data.get("offsetZ", Double.class);
        double extra = data.get("extra", Double.class);

        return new JumpParticle(particle, color, size, count, offsetX, offsetY, offsetZ, extra);
    }
}
