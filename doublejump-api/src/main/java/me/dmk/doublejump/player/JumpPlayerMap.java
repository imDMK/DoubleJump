package me.dmk.doublejump.player;

import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A collection that has players who have double jump mode enabled.
 */
public class JumpPlayerMap {

    private final Map<UUID, JumpPlayer> jumpPlayers = new ConcurrentHashMap<>();

    public void add(Player player) {
        this.jumpPlayers.put(player.getUniqueId(), new JumpPlayer());
    }

    public boolean contains(Player player) {
        return this.jumpPlayers.containsKey(player.getUniqueId());
    }

    public void remove(Player player) {
        this.jumpPlayers.remove(player.getUniqueId());
    }

    public void remove(UUID uuid) {
        this.jumpPlayers.remove(uuid);
    }

    public Optional<JumpPlayer> get(Player player) {
        return Optional.ofNullable(
                this.jumpPlayers.get(player.getUniqueId())
        );
    }

    public Map<UUID, JumpPlayer> getJumpPlayers() {
        return Collections.unmodifiableMap(this.jumpPlayers);
    }
}
