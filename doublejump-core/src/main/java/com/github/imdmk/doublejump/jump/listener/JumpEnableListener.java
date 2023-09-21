package com.github.imdmk.doublejump.jump.listener;

import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.jump.JumpPlayerService;
import com.github.imdmk.doublejump.jump.JumpSettings;
import com.github.imdmk.doublejump.jump.event.DoubleJumpEvent;
import com.github.imdmk.doublejump.notification.Notification;
import com.github.imdmk.doublejump.notification.NotificationSender;
import com.github.imdmk.doublejump.notification.NotificationSettings;
import com.github.imdmk.doublejump.region.RegionProvider;
import com.github.imdmk.doublejump.scheduler.TaskScheduler;
import com.github.imdmk.doublejump.util.DurationUtil;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

public class JumpEnableListener implements Listener {

    private final Plugin plugin;
    private final Server server;
    private final JumpSettings jumpSettings;
    private final NotificationSettings notificationSettings;
    private final JumpPlayerManager jumpPlayerManager;
    private final JumpPlayerService jumpPlayerService;
    private final NotificationSender notificationSender;
    private final TaskScheduler taskScheduler;
    private final RegionProvider regionProvider;

    public JumpEnableListener(Plugin plugin, Server server, JumpSettings jumpSettings, NotificationSettings notificationSettings, JumpPlayerManager jumpPlayerManager, JumpPlayerService jumpPlayerService, NotificationSender notificationSender, TaskScheduler taskScheduler, RegionProvider regionProvider) {
        this.plugin = plugin;
        this.server = server;
        this.jumpSettings = jumpSettings;
        this.notificationSettings = notificationSettings;
        this.jumpPlayerManager = jumpPlayerManager;
        this.jumpPlayerService = jumpPlayerService;
        this.notificationSender = notificationSender;
        this.taskScheduler = taskScheduler;
        this.regionProvider = regionProvider;
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();

        GameMode playerGameMode = player.getGameMode();
        World playerWorld = player.getWorld();

        Optional<JumpPlayer> jumpPlayerOptional = this.jumpPlayerManager.getJumpPlayer(player.getUniqueId());
        if (jumpPlayerOptional.isEmpty()) {
            return;
        }

        JumpPlayer jumpPlayer = jumpPlayerOptional.get();

        event.setCancelled(true);

        player.setFlying(false);
        player.setAllowFlight(false);

        if (jumpPlayer.isDelay()) {
            Notification jumpDelayNotification = Notification.builder()
                    .fromNotification(this.notificationSettings.jumpDelayNotification)
                    .placeholder("{TIME}", DurationUtil.toHumanReadable(jumpPlayer.getRemainingDelayDuration()))
                    .build();

            this.notificationSender.send(player, jumpDelayNotification);
            return;
        }

        if (!jumpPlayer.hasJumps()) {
            if (this.jumpSettings.limitSettings.regenerationDelay.isZero()) {
                this.notificationSender.send(player, this.notificationSettings.jumpLimitNotification);
                return;
            }

            Notification jumpLimitDelayNotification = Notification.builder()
                    .fromNotification(this.notificationSettings.jumpLimitDelayNotification)
                    .placeholder("{TIME}", DurationUtil.toHumanReadable(jumpPlayer.getRemainingJumpRegenerationDuration()))
                    .build();

            this.notificationSender.send(player, jumpLimitDelayNotification);
            return;
        }

        if (this.regionProvider.isInRegion(player)) {
            this.notificationSender.send(player, this.notificationSettings.jumpModeDisableRegionNotification);
            return;
        }

        if (this.jumpSettings.restrictionsSettings.disabledGameModes.contains(playerGameMode)) {
            this.notificationSender.send(player, this.notificationSettings.jumpModeDisabledGameModeNotification);
            return;
        }

        if (this.jumpSettings.restrictionsSettings.disabledWorlds.contains(playerWorld.getName())) {
            this.notificationSender.send(player, this.notificationSettings.jumpModeDisabledWorldNotification);
            return;
        }

        DoubleJumpEvent doubleJumpEvent = new DoubleJumpEvent(player, jumpPlayer);

        this.server.getPluginManager().callEvent(doubleJumpEvent);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.isFlying()) {
            return;
        }

        Optional<JumpPlayer> jumpPlayerOptional = this.jumpPlayerManager.getJumpPlayer(player.getUniqueId());
        if (jumpPlayerOptional.isEmpty()) {
            return;
        }

        JumpPlayer jumpPlayer = jumpPlayerOptional.get();

        if (jumpPlayer.isDelay()) {
            if (player.hasMetadata("jumpDelayNotificationSent")) {
                return;
            }

            player.setMetadata("jumpDelayNotificationSent", new FixedMetadataValue(this.plugin, true));

            Notification jumpDelayNotification = Notification.builder()
                    .fromNotification(this.notificationSettings.jumpDelayNotification)
                    .placeholder("{TIME}", DurationUtil.toHumanReadable(jumpPlayer.getRemainingDelayDuration()))
                    .build();

            this.notificationSender.send(player, jumpDelayNotification);
            return;
        }

        if (!jumpPlayer.hasJumps()) {
            if (player.hasMetadata("jumpLimitNotificationSent")) {
                return;
            }

            player.setMetadata("jumpLimitNotificationSent", new FixedMetadataValue(this.plugin, true));

            if (this.jumpSettings.limitSettings.regenerationDelay.isZero()) {
                this.notificationSender.send(player, this.notificationSettings.jumpLimitNotification);
                return;
            }

            Notification jumpLimitDelayNotification = Notification.builder()
                    .fromNotification(this.notificationSettings.jumpLimitDelayNotification)
                    .placeholder("{TIME}", DurationUtil.toHumanReadable(jumpPlayer.getRemainingJumpRegenerationDuration()))
                    .build();

            this.notificationSender.send(player, jumpLimitDelayNotification);
            return;
        }

        player.removeMetadata("jumpDelayNotificationSent", this.plugin);
        player.removeMetadata("jumpLimitNotificationSent", this.plugin);

        if (!player.getAllowFlight()) {
            player.setAllowFlight(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        GameMode playerGameMode = player.getGameMode();
        String playerWorldName = player.getWorld().getName();

        if (this.regionProvider.isInRegion(player)) {
            return;
        }

        if (this.jumpSettings.restrictionsSettings.disabledGameModes.contains(playerGameMode)) {
            return;
        }

        if (this.jumpSettings.restrictionsSettings.disabledWorlds.contains(playerWorldName)) {
            return;
        }

        if (this.jumpSettings.enableJumpModeOnJoinForPlayers || this.jumpSettings.enableJumpModeOnJoinForAdmins && player.isOp()) {
            this.taskScheduler.runLaterAsync(() -> this.jumpPlayerService.enable(player, true), 40L);
        }
    }
}
