package com.github.imdmk.doublejump.feature.jump;

import com.github.imdmk.doublejump.task.TaskScheduler;
import com.github.imdmk.doublejump.util.GameModeUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;

/**
 * Service responsible for managing the flight state of players.
 * Used to enable or disable flight for both double jump and general flight scenarios.
 */
public class PlayerFlyingService {

    @Inject private TaskScheduler taskScheduler;

    /**
     * Enables flight for the player without altering flying state.
     *
     * @param player The player to modify.
     */
    public void enable(@NotNull final Player player) {
        this.taskScheduler.run(() -> player.setAllowFlight(true));
    }

    /**
     * Disables flight for the player and sets allowFlight according to player's gameMode
     *
     * @param player The player to modify.
     */
    public void disable(@NotNull final Player player) {
        this.disable(player, GameModeUtil.isFlyingGameMode(player.getGameMode()));
    }

    /**
     * Disables the player's current flying state.
     * The player's permission to fly in the future is set according to the provided flag.
     *
     * @param player      the player whose flying state will be disabled; must not be null
     * @param allowFlight true to allow the player to start flying later, false to revoke flight permission
     */
    public void disable(@NotNull final Player player, final boolean allowFlight) {
        this.taskScheduler.run(() -> {
            player.setFlying(false);
            player.setAllowFlight(allowFlight);
        });
    }
}
