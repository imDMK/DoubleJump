package com.github.imdmk.doublejump.jump.feature.visual.configuration;

import com.github.imdmk.doublejump.jump.feature.visual.JumpVisual;
import com.github.imdmk.doublejump.jump.feature.visual.particle.JumpParticle;
import com.github.imdmk.doublejump.jump.feature.visual.sound.JumpSound;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JumpVisualSerializer implements ObjectSerializer<JumpVisual> {

    @Override
    public boolean supports(@NotNull Class<? super JumpVisual> type) {
        return JumpVisual.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NotNull JumpVisual visual, @NotNull SerializationData data, @NotNull GenericsDeclaration generics) {
        if (!visual.getJumpParticles().isEmpty()) {
            data.addCollection("jumpParticles", visual.getJumpParticles(), JumpParticle.class);
        }

        visual.getJumpSound()
                .ifPresent(sound -> data.add("jumpSound", sound, JumpSound.class));
    }

    @Override
    public JumpVisual deserialize(@NotNull DeserializationData data, @NotNull GenericsDeclaration generics) {
        List<JumpParticle> particles = data.getAsList("jumpParticles", JumpParticle.class);
        JumpSound sound = data.get("jumpSound", JumpSound.class);
        return new JumpVisual(particles, sound);
    }
}
