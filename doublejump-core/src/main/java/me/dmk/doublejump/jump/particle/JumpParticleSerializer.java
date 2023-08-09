package me.dmk.doublejump.jump.particle;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.bukkit.Particle;
import org.checkerframework.checker.nullness.qual.NonNull;

public class JumpParticleSerializer implements ObjectSerializer<JumpParticle> {

    @Override
    public boolean supports(@NonNull Class<? super JumpParticle> type) {
        return JumpParticle.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull JumpParticle jumpParticle, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("type", jumpParticle.getType(), Particle.class);
        data.add("color", jumpParticle.getColor(), String.class);
        data.add("size", jumpParticle.getSize(), Integer.class);
    }

    @Override
    public JumpParticle deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        Particle type = data.get("type", Particle.class);
        String color = data.get("color", String.class);
        int size = data.get("size", Integer.class);

        return new JumpParticle(type, color, size);
    }
}
