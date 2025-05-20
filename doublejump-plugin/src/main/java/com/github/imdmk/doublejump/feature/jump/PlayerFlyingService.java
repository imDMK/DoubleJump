package com.github.imdmk.doublejump.feature.jump;

import com.github.imdmk.doublejump.task.TaskScheduler;
import org.bukkit.entity.Player;
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
    public void enable(final Player player) {
        this.taskScheduler.run(() -> player.setAllowFlight(true));
    }

    /**
     * Disables flight for the player and sets allowFlight to false.
     *
     * @param player The player to modify.
     */
    public void disable(final Player player) {
        this.disable(player, false);
    }

    /**
     * Disables flying for the player. Optional keeps allowFlight enabled.
     *
     * @param player           The player to modify.
     * @param keepAllowFlight  If true, allowFlight will not be changed.
     */
    public void disable(final Player player, final boolean keepAllowFlight) {
        this.taskScheduler.run(() -> {
            player.setFlying(false);
            if (!keepAllowFlight) {
                player.setAllowFlight(false);
            }
        });
    }
}
