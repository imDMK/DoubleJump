package com.github.imdmk.doublejump.jump.listener;

import com.github.imdmk.doublejump.jump.JumpPlayerService;
import com.github.imdmk.doublejump.jump.JumpSettings;
import com.github.imdmk.doublejump.notification.NotificationSender;
import com.github.imdmk.doublejump.notification.NotificationSettings;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JumpDisableListener implements Listener {

    private final JumpSettings jumpSettings;
    private final NotificationSettings notificationSettings;
    private final NotificationSender notificationSender;
    private final JumpPlayerService jumpPlayerService;

    public JumpDisableListener(JumpSettings jumpSettings, NotificationSettings notificationSettings, NotificationSender notificationSender, JumpPlayerService jumpPlayerService) {
        this.jumpSettings = jumpSettings;
        this.notificationSettings = notificationSettings;
        this.notificationSender = notificationSender;
        this.jumpPlayerService = jumpPlayerService;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        this.jumpPlayerService.disable(player);
    }

    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        GameMode newGameMode = event.getNewGameMode();

        if (this.jumpSettings.restrictionsSettings.disabledGameModes.contains(newGameMode)) {
            this.jumpPlayerService.disable(player);

            this.notificationSender.send(player, this.notificationSettings.jumpModeDisabledGameModeNotification);
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        String playerWorldName = player.getWorld().getName();

        if (this.jumpSettings.restrictionsSettings.disabledWorlds.contains(playerWorldName)) {
            this.jumpPlayerService.disable(player);

            this.notificationSender.send(player, this.notificationSettings.jumpModeDisabledWorldNotification);
        }
    }
}
