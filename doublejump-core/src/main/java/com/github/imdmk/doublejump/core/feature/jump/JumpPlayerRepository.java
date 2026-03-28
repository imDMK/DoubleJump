package com.github.imdmk.doublejump.core.feature.jump;

import com.github.imdmk.doublejump.core.injector.ComponentPriority;
import com.github.imdmk.doublejump.core.injector.annotations.Service;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service(priority = ComponentPriority.LOWEST)
public final class JumpPlayerRepository {

    private final Map<UUID, JumpPlayer> activePlayers = new ConcurrentHashMap<>();

    public void activate(JumpPlayer player) {
        activePlayers.put(player.getUuid(), player);
    }

    public JumpPlayer deactivate(UUID uuid) {
        return activePlayers.remove(uuid);
    }

    public boolean isActive(UUID uuid) {
        return activePlayers.containsKey(uuid);
    }

    public JumpPlayer get(UUID uuid) {
        return activePlayers.get(uuid);
    }

    public JumpPlayer getOrThrow(UUID uuid) {
        JumpPlayer player = activePlayers.get(uuid);
        if (player == null) {
            throw new IllegalStateException("JumpPlayer not active: " + uuid);
        }

        return player;
    }

    public Collection<JumpPlayer> getActivePlayers() {
        return activePlayers.values();
    }
}
