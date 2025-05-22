package com.github.imdmk.doublejump.feature.jump.controller;

import com.github.imdmk.doublejump.injector.PluginListener;
import com.github.imdmk.doublejump.jump.JumpPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class JumpPlayerSessionController extends PluginListener {

    /**
     * Creates a JumpPlayer session when a player joins the server.
     *
     * @param event Player join event.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    void onPlayerJoin(final PlayerJoinEvent event) {
        this.createJumpPlayer(event.getPlayer());
    }

    /**
     * Reinitializes JumpPlayer sessions for all online players on server reload.
     *
     * @param event The server load event.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    void onServerReload(final ServerLoadEvent event) {
        if (event.getType() != ServerLoadEvent.LoadType.RELOAD) {
            return;
        }

        this.server.getOnlinePlayers().forEach(this::createJumpPlayer);
    }

    /**
     * Removes a player's JumpPlayer session when they quit the server.
     *
     * @param event Player quit event.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    void onPlayerQuit(final PlayerQuitEvent event) {
        this.jumpCache.remove(event.getPlayer().getUniqueId());
    }

    /**
     * Adds a player to the jump cache by creating a new JumpPlayer instance.
     *
     * @param player The player to add.
     */
    private void createJumpPlayer(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        this.jumpCache.put(uuid, new JumpPlayer(uuid, player.getName()));
    }
}
