package com.github.imdmk.doublejump.jump;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class JumpPlayer {

    private final UUID uuid;

    private boolean active;
    private Instant lastJump;

    public JumpPlayer(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    public @NotNull UUID getUuid() {
        return this.uuid;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Optional<Instant> getLastJump() {
        return Optional.ofNullable(this.lastJump);
    }

    public void setLastJump(@NotNull Instant lastJump) {
        this.lastJump = lastJump;
    }
}
