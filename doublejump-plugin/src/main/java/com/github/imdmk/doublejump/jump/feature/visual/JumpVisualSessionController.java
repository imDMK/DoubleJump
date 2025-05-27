package com.github.imdmk.doublejump.jump.feature.visual;

import com.github.imdmk.doublejump.infrastructure.injector.PluginListener;
import com.github.imdmk.doublejump.jump.feature.visual.repository.JumpVisualCache;
import com.github.imdmk.doublejump.jump.feature.visual.repository.JumpVisualRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.UUID;
import java.util.logging.Level;

public class JumpVisualSessionController extends PluginListener {

    @Inject private JumpVisualCache visualCache;
    @Inject private JumpVisualRepository visualRepository;
    @Inject private JumpVisualService visualService;

    @EventHandler(priority = EventPriority.HIGHEST)
    void onPlayerQuit(final PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        this.visualCache.getByUuid(uuid)
                .ifPresent(settings -> this.saveVisual(uuid, settings));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    void onPlayerJoin(final PlayerJoinEvent event) {
        this.createVisual(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    void onServerReload(ServerLoadEvent event) {
        if (event.getType() != ServerLoadEvent.LoadType.RELOAD) {
            return;
        }

        this.server.getOnlinePlayers().forEach(this::createVisual);
    }

    private void createVisual(@NotNull Player player) {
        this.visualService.getOrCreate(player.getUniqueId())
                .exceptionally(throwable -> {
                    this.logger.log(Level.SEVERE, "An error occurred while trying to get the visual settings for player: " + player.getUniqueId(), throwable);
                    return null;
                });
    }

    private void saveVisual(@NotNull UUID uuid, @NotNull JumpVisual visual) {
        this.visualRepository.save(uuid, visual)
                .exceptionally(throwable -> {
                    this.logger.log(Level.SEVERE, "An error occurred while trying to save the visual settings: " + uuid, throwable);
                    return null;
                });
    }
}
