package me.dmk.doublejump.command;

import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import me.dmk.doublejump.configuration.PluginConfiguration;
import me.dmk.doublejump.notification.NotificationSender;
import me.dmk.doublejump.player.JumpPlayerManager;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

@Route(name = "doublejump")
public class DoubleJumpCommand {

    private final PluginConfiguration pluginConfiguration;
    private final NotificationSender notificationSender;
    private final JumpPlayerManager jumpPlayerManager;

    public DoubleJumpCommand(PluginConfiguration pluginConfiguration, NotificationSender notificationSender, JumpPlayerManager jumpPlayerManager) {
        this.pluginConfiguration = pluginConfiguration;
        this.notificationSender = notificationSender;
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @Async
    @Execute
    void execute(Player player) {
        GameMode playerGameMode = player.getGameMode();
        World playerWorld = player.getWorld();

        if (this.pluginConfiguration.getDisabledGameModes().contains(playerGameMode)) {
            this.notificationSender.sendMessage(player, this.pluginConfiguration.getjumpModeDisabledGameModeNotification());
            return;
        }

        if (this.pluginConfiguration.getDisabledWorlds().contains(playerWorld.getName())) {
            this.notificationSender.sendMessage(player, this.pluginConfiguration.getjumpModeDisabledWorldNotification());
            return;
        }

        if (this.jumpPlayerManager.isDoubleJumpMode(player)) {
            this.jumpPlayerManager.disable(player);
            this.notificationSender.sendMessage(player, this.pluginConfiguration.getjumpModeDisabledNotification());
        }
        else {
            this.jumpPlayerManager.enable(player);
            this.notificationSender.sendMessage(player, this.pluginConfiguration.getjumpModeEnabledNotification());
        }
    }
}
