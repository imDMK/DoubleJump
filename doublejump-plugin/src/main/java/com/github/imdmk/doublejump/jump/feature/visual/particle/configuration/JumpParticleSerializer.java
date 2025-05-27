package com.github.imdmk.doublejump.jump.feature.visual.particle.configuration;

import com.github.imdmk.doublejump.jump.feature.visual.particle.JumpParticle;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
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
    }

    @Override
    public JumpParticle deserialize(@NotNull DeserializationData data, @NotNull GenericsDeclaration generics) {
        Particle particle = data.get("particle", Particle.class);
        int count = data.get("count", Integer.class);
        return new JumpParticle(particle, count);
    }
}
