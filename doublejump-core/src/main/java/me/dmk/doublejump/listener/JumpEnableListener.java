package me.dmk.doublejump.listener;

import me.dmk.doublejump.configuration.JumpConfiguration;
import me.dmk.doublejump.configuration.MessageConfiguration;
import me.dmk.doublejump.event.DoubleJumpEvent;
import me.dmk.doublejump.notification.Notification;
import me.dmk.doublejump.notification.NotificationSender;
import me.dmk.doublejump.player.JumpPlayer;
import me.dmk.doublejump.player.JumpPlayerManager;
import me.dmk.doublejump.task.scheduler.TaskScheduler;
import me.dmk.doublejump.util.DurationUtil;
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

    public JumpEnableListener(Server server, JumpConfiguration jumpConfiguration, MessageConfiguration messageConfiguration, JumpPlayerManager jumpPlayerManager, NotificationSender notificationSender, TaskScheduler taskScheduler) {
        this.server = server;
        this.jumpConfiguration = jumpConfiguration;
        this.messageConfiguration = messageConfiguration;
        this.jumpPlayerManager = jumpPlayerManager;
        this.notificationSender = notificationSender;
        this.taskScheduler = taskScheduler;
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

        if (!jumpPlayer.canUseJump()) {
            Notification notification = Notification.builder()
                    .fromNotification(this.messageConfiguration.jumpDelayNotification)
                    .placeholder("{TIME}", DurationUtil.toHumanReadable(jumpPlayer.getRemainingDelayDuration()))
                    .build();

            this.notificationSender.sendMessage(player, notification);
            return;
        }

        if (this.jumpConfiguration.disabledGameModes.contains(playerGameMode)) {
            this.jumpPlayerManager.remove(player.getUniqueId());
            this.notificationSender.sendMessage(player, this.messageConfiguration.jumpModeDisabledGameModeNotification);
            return;
        }

        if (this.jumpConfiguration.disabledWorlds.contains(playerWorld.getName())) {
            this.jumpPlayerManager.remove(player.getUniqueId());
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
