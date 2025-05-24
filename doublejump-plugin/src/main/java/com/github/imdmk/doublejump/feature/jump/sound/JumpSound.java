package com.github.imdmk.doublejump.feature.jump.sound;

import org.bukkit.Sound;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class JumpSound {

    private final Sound sound;
    private final float volume;
    private final float pitch;

    private JumpSound(@NotNull Builder builder) {
        this.sound = builder.sound;
        this.volume = builder.volume;
        this.pitch = builder.pitch;
    }

    public @NotNull Sound sound() {
        return this.sound;
    }

    public float volume() {
        return this.volume;
    }

    public float pitch() {
        return this.pitch;
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Sound sound;
        private float volume = 1.0f;
        private float pitch = 1.0f;

        private Builder() {}

        @Contract("_-> this")
        public Builder sound(@NotNull Sound sound) {
            this.sound = Objects.requireNonNull(sound, "sound cannot be null");
            return this;
        }

        @Contract("_-> this")
        public Builder volume(float volume) {
            this.volume = volume;
            return this;
        }

        @Contract("_-> this")
        public Builder pitch(float pitch) {
            this.pitch = pitch;
            return this;
        }

        public @NotNull JumpSound build() {
            return new JumpSound(this);
        }
    }
}
