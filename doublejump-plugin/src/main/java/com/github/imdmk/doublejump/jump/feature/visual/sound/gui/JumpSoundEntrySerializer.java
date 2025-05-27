package com.github.imdmk.doublejump.jump.feature.visual.sound.gui;

import com.github.imdmk.doublejump.jump.feature.visual.sound.JumpSound;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class JumpSoundEntrySerializer implements ObjectSerializer<JumpSoundEntry> {

    @Override
    public boolean supports(@NotNull Class<? super JumpSoundEntry> type) {
        return JumpSoundEntry.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NotNull JumpSoundEntry soundEntry, @NotNull SerializationData data, @NotNull GenericsDeclaration generics) {
        data.add("sound", soundEntry.jumpSound(), JumpSound.class);
        data.add("displayItem", soundEntry.displayItem(), Material.class);
    }

    @Override
    public JumpSoundEntry deserialize(@NotNull DeserializationData data, @NotNull GenericsDeclaration generics) {
        JumpSound sound = data.get("sound", JumpSound.class);
        Material displayItem = data.get("displayItem", Material.class);
        return new JumpSoundEntry(sound, displayItem);
    }
}
