package com.github.imdmk.doublejump.jump.sound;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.bukkit.Sound;
import org.checkerframework.checker.nullness.qual.NonNull;

public class JumpSoundSerializer implements ObjectSerializer<JumpSound> {

    @Override
    public boolean supports(@NonNull Class<? super JumpSound> type) {
        return JumpSound.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull JumpSound jumpSound, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("sound", jumpSound.sound(), Sound.class);
        data.add("volume", jumpSound.volume(), float.class);
        data.add("pitch", jumpSound.pitch(), float.class);
    }

    @Override
    public JumpSound deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        Sound sound = data.get("sound", Sound.class);
        float volume = data.get("volume", float.class);
        float pitch = data.get("pitch", float.class);

        return new JumpSound(sound, volume, pitch);
    }
}
