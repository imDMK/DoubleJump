package com.github.imdmk.doublejump.core.feature.jump;

import java.util.UUID;

public final class JumpPlayer {

    private final UUID uuid;
    private final JumpActivationType activationType;

    private JumpPlayer(
            UUID uuid,
            JumpActivationType activationType
    ) {
        this.uuid = uuid;
        this.activationType = activationType;
    }

    public static JumpPlayer create(UUID uuid, JumpActivationType type) {
        return new JumpPlayer(uuid, type);
    }

    public UUID getUuid() {
        return uuid;
    }

    public JumpActivationType getActivationType() {
        return activationType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JumpPlayer that = (JumpPlayer) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}