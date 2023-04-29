package me.dmk.doublejump.player;

import me.dmk.doublejump.DoubleJump;
import me.dmk.doublejump.configuration.PluginConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class JumpPlayerManager {

    private final DoubleJump doubleJump;
    private final PluginConfiguration pluginConfiguration;
    private final JumpPlayerMap jumpPlayerMap;

    public JumpPlayerManager(DoubleJump doubleJump, PluginConfiguration pluginConfiguration, JumpPlayerMap jumpPlayerMap) {
        this.doubleJump = doubleJump;
        this.pluginConfiguration = pluginConfiguration;
        this.jumpPlayerMap = jumpPlayerMap;
    }

    public boolean enable(Player player) {
        if (!this.canUseDoubleJump(player)) {
            return false;
        }

        player.setFlying(false);
        player.setAllowFlight(true);

        this.jumpPlayerMap.add(player);
        return true;
    }

    public void enableOnJoin(Player player, boolean async) {
        if (
                player.isOp() && this.pluginConfiguration.isJumpEnableOnJoinForAdmins()
                        || !player.isOp() && this.canUseDoubleJump(player)
                        && this.pluginConfiguration.isJumpEnableOnJoin()
        ) {
            if (async) {
                Bukkit.getScheduler().runTaskLaterAsynchronously(
                        this.doubleJump,
                        () -> this.enable(player),
                        40L
                );
            } else {
                this.enable(player);
            }
        }
    }


    public void refresh(Player player) {
        if (!this.isDoubleJumpMode(player)) {
            return;
        }

        if (!this.canUseDoubleJump(player)) {
            return;
        }

        player.setFlying(false);
        player.setAllowFlight(true);
    }

    public void disable(Player player) {
        GameMode playerGameMode = player.getGameMode();

        this.jumpPlayerMap.remove(player);

        if (playerGameMode == GameMode.SURVIVAL || playerGameMode == GameMode.ADVENTURE) {
            player.setAllowFlight(false);
        }
    }

    public boolean isDoubleJumpMode(Player player) {
        return this.jumpPlayerMap.contains(player);
    }

    public boolean canUseDoubleJump(Player player) {
        GameMode playerGameMode = player.getGameMode();
        World playerWorld = player.getWorld();

        if (this.pluginConfiguration.getDisabledGameModes().contains(playerGameMode)) {
            return false;
        }

        if (this.pluginConfiguration.getDisabledWorlds().contains(playerWorld.getName())) {
            return false;
        }

        String jumpPermission = this.pluginConfiguration.getDoubleJumpUsePermission();
        if (jumpPermission == null || jumpPermission.isEmpty()) {
            return true;
        }

        return player.hasPermission(jumpPermission);
    }
}
