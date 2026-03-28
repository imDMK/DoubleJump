package com.github.imdmk.doublejump.core.config.serializer;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;

public final class ParticleSerializer implements ObjectSerializer<Particle> {

    @Override
    public boolean supports(@NotNull Class<? super Particle> type) {
        return Particle.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(
            @NotNull Particle particle,
            @NotNull SerializationData data,
            @NotNull GenericsDeclaration generics
    ) {
        data.setValue(particle.getKey().getKey(), String.class);
    }

    @Override
    public Particle deserialize(
            @NotNull DeserializationData data,
            @NotNull GenericsDeclaration generics
    ) {
        String raw = data.getValue(String.class);
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Particle cannot be null or empty");
        }

        NamespacedKey key = NamespacedKey.fromString(raw);
        if (key == null) {
            throw new IllegalArgumentException("Invalid particle key: " + raw);
        }

        Particle particle = Registry.PARTICLE_TYPE.get(key);
        if (particle == null) {
            throw new IllegalArgumentException("Unknown particle: " + raw);
        }

        return particle;
    }
}