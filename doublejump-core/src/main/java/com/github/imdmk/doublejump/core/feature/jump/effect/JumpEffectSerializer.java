package com.github.imdmk.doublejump.core.feature.jump.effect;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

public final class JumpEffectSerializer implements ObjectSerializer<JumpEffect> {

    @Override
    public boolean supports(@NotNull Class<? super JumpEffect> type) {
        return JumpEffect.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(
            @NotNull JumpEffect effect,
            @NotNull SerializationData data,
            @NotNull GenericsDeclaration generics
    ) {
        data.add("type", effect.type(), JumpEffectType.class);

        if (effect instanceof ParticleEffect(Particle particle, int count, double offset, double extra)) {
            data.add("particle", particle, Particle.class);
            data.add("count", count, int.class);
            data.add("offset", offset, double.class);
            data.add("extra", extra, double.class);
        }
        else if (effect instanceof SoundEffect(Sound sound, float volume, float pitch)) {
            data.add("sound", sound, Sound.class);
            data.add("volume", volume, float.class);
            data.add("pitch", pitch, float.class);
        }
        else {
            throw new IllegalArgumentException("Unknown JumpEffect: " + effect.getClass().getName());
        }
    }

    @Override
    public JumpEffect deserialize(
            @NotNull DeserializationData data,
            @NotNull GenericsDeclaration generics
    ) {
        JumpEffectType type = data.get("type", JumpEffectType.class);

        if (type == null) {
            throw new IllegalArgumentException("Missing 'type' in JumpEffect config");
        }

        if (type == JumpEffectType.PARTICLE) {
            Particle particle = data.get("particle", Particle.class);
            int count = data.get("count", int.class);
            double offset = data.get("offset", double.class);
            double extra = data.get("extra", double.class);

            return new ParticleEffect(particle, count, offset, extra);
        }
        else if (type == JumpEffectType.SOUND) {
            Sound sound = data.get("sound", Sound.class);
            float volume = data.get("volume", float.class);
            float pitch = data.get("pitch", float.class);

            return new SoundEffect(sound, volume, pitch);
        }

        throw new IllegalStateException("Unsupported JumpEffectType: " + type);
    }
}
