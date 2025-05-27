package com.github.imdmk.doublejump.jump.feature.visual.particle.gui;

import com.github.imdmk.doublejump.jump.feature.visual.particle.JumpParticle;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class JumpParticleEntrySerializer implements ObjectSerializer<JumpParticleEntry> {

    @Override
    public boolean supports(@NotNull Class<? super JumpParticleEntry> type) {
        return JumpParticleEntry.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NotNull JumpParticleEntry guiEntry, @NotNull SerializationData data, @NotNull GenericsDeclaration generics) {
        data.add("particle", guiEntry.particle(), JumpParticle.class);
        data.add("displayItem", guiEntry.displayItem(), Material.class);
    }

    @Override
    public JumpParticleEntry deserialize(@NotNull DeserializationData data, @NotNull GenericsDeclaration generics) {
        JumpParticle particle = data.get("particle", JumpParticle.class);
        Material displayItem = data.get("displayItem", Material.class);
        return new JumpParticleEntry(particle, displayItem);
    }
}
