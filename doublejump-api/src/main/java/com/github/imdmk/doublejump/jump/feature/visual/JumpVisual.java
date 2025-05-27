package com.github.imdmk.doublejump.jump.feature.visual;

import com.github.imdmk.doublejump.jump.feature.visual.particle.JumpParticle;
import com.github.imdmk.doublejump.jump.feature.visual.sound.JumpSound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents visual effects (particles and sound) triggered when a player performs a jump.
 */
public final class JumpVisual {

    private List<JumpParticle> jumpParticles;
    private JumpSound jumpSound;

    /**
     * Constructs a new {@code JumpVisual} with optional particles and sound.
     *
     * @param particles list of jump particles (nullable)
     * @param sound     jump to sound (nullable)
     */
    public JumpVisual(@Nullable List<JumpParticle> particles, @Nullable JumpSound sound) {
        this.jumpParticles = particles != null ? List.copyOf(particles) : Collections.emptyList();
        this.jumpSound = sound;
    }

    /**
     * Returns the list of jump particles.
     *
     * @return non-null, unmodifiable list
     */
    public @NotNull List<JumpParticle> getJumpParticles() {
        return this.jumpParticles;
    }

    /**
     * Sets the jump particles list.
     *
     * @param particles nullable list
     */
    public void setJumpParticles(@NotNull List<JumpParticle> particles) {
        this.jumpParticles = List.copyOf(particles);
    }

    /**
     * Adds a jump particle to the current list.
     *
     * @param particle the particle to add, must not be null
     */
    public void addJumpParticle(@NotNull JumpParticle particle) {
        List<JumpParticle> updated = new ArrayList<>(this.jumpParticles);
        updated.add(particle);
        this.jumpParticles = Collections.unmodifiableList(updated);
    }

    /**
     * Removes a jump particle from the current list.
     *
     * @param particle the particle to remove, must not be null
     * @return true if the particle was found and removed, false otherwise
     */
    public boolean removeJumpParticle(@NotNull JumpParticle particle) {
        if (this.jumpParticles == null || !this.jumpParticles.contains(particle)) {
            return false;
        }

        List<JumpParticle> updated = new ArrayList<>(this.jumpParticles);
        boolean removed = updated.remove(particle);
        this.jumpParticles = Collections.unmodifiableList(updated);
        return removed;
    }

    /**
     * Returns the optional jump sound.
     *
     * @return {@link Optional} containing jump sound or empty
     */
    public Optional<JumpSound> getJumpSound() {
        return Optional.ofNullable(this.jumpSound);
    }

    public boolean isJumpSound(@NotNull JumpSound jumpSound) {
        return this.getJumpSound()
                .map(sound -> sound.equals(jumpSound))
                .orElse(false);
    }

    /**
     * Sets the jump sound.
     *
     * @param sound nullable sound
     */
    public void setJumpSound(@Nullable JumpSound sound) {
        this.jumpSound = sound;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof JumpVisual that)) {
            return false;
        }
        return Objects.equals(this.jumpParticles, that.jumpParticles)
                && Objects.equals(this.jumpSound, that.jumpSound);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.jumpParticles, this.jumpSound);
    }

    @Override
    public String toString() {
        return "JumpVisual{" +
                "jumpParticles=" + this.jumpParticles +
                ", jumpSound=" + this.jumpSound +
                '}';
    }
}
