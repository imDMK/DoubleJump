package com.github.imdmk.doublejump.jump;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class JumpPlayer {

    private final UUID uuid;
    private final String name;

    private boolean active;

    public JumpPlayer(@NotNull UUID uuid, @NotNull String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public @NotNull UUID getUuid() {
        return this.uuid;
    }

    public @NotNull String getName() {
        return this.name;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
