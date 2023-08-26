package com.github.imdmk.doublejump.jump.listener;

import com.github.imdmk.doublejump.configuration.MessageConfiguration;
import com.github.imdmk.doublejump.jump.JumpConfiguration;
import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.jump.event.DoubleJumpEvent;
import com.github.imdmk.doublejump.notification.Notification;
import com.github.imdmk.doublejump.notification.NotificationSender;
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

import java.util.Optional;

public class JumpEnableListener implements Listener {

    private final Server server;
    private final JumpConfiguration jumpConfiguration;
    private final MessageConfiguration messageConfiguration;
    private final JumpPlayerManager jumpPlayerManager;
    private final NotificationSender notificationSender;
    private final TaskScheduler taskScheduler;
    private final RegionProvider regionProvider;

    public JumpEnableListener(Server server, JumpConfiguration jumpConfiguration, MessageConfiguration messageConfiguration, JumpPlayerManager jumpPlayerManager, NotificationSender notificationSender, TaskScheduler taskScheduler, RegionProvider regionProvider) {
        this.server = server;
        this.jumpConfiguration = jumpConfiguration;
        this.messageConfiguration = messageConfiguration;
        this.jumpPlayerManager = jumpPlayerManager;
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

        if (!jumpPlayer.canUseJump()) {
            Notification notification = Notification.builder()
                    .fromNotification(this.messageConfiguration.jumpDelayNotification)
                    .placeholder("{TIME}", DurationUtil.toHumanReadable(jumpPlayer.getRemainingDelayDuration()))
                    .build();

            this.notificationSender.sendMessage(player, notification);
            return;
        }

        if (this.regionProvider.isInRegion(player)) {
            this.notificationSender.sendMessage(player, this.messageConfiguration.jumpModeDisableRegionNotification);
            return;
        }

        if (this.jumpConfiguration.disabledGameModes.contains(playerGameMode)) {
            this.notificationSender.sendMessage(player, this.messageConfiguration.jumpModeDisabledGameModeNotification);
            return;
        }

        if (this.jumpConfiguration.disabledWorlds.contains(playerWorld.getName())) {
            this.notificationSender.sendMessage(player, this.messageConfiguration.jumpModeDisabledWorldNotification);
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

        if (!jumpPlayer.canUseJump()) {
            return;
        }

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

        if (this.jumpConfiguration.disabledGameModes.contains(playerGameMode)) {
            return;
        }

        if (this.jumpConfiguration.disabledWorlds.contains(playerWorldName)) {
            return;
        }

        if (this.jumpConfiguration.enableJumpModeOnJoinForPlayers || this.jumpConfiguration.enableJumpModeOnJoinForAdmins && player.isOp()) {
            this.taskScheduler.runLaterAsync(() -> this.jumpPlayerManager.enable(player, true), 40L);
        }
    }
}
