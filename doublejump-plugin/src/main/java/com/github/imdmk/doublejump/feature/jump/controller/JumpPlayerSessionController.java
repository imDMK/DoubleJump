package com.github.imdmk.doublejump.feature.jump.controller;

import com.github.imdmk.doublejump.injector.PluginListener;
import com.github.imdmk.doublejump.jump.JumpPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class JumpPlayerSessionController extends PluginListener {

    @EventHandler(priority = EventPriority.HIGHEST)
    void onPlayerJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String name = player.getName();

        this.jumpCache.put(uuid, new JumpPlayer(uuid, name));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    void onPlayerQuit(final PlayerQuitEvent event) {
        this.jumpCache.remove(event.getPlayer().getUniqueId());
    }
}
