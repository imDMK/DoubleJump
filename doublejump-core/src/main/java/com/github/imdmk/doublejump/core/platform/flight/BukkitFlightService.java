package com.github.imdmk.doublejump.core.platform.flight;

import com.github.imdmk.doublejump.core.injector.ComponentPriority;
import com.github.imdmk.doublejump.core.injector.annotations.Service;
import com.github.imdmk.doublejump.core.platform.scheduler.TaskScheduler;
import org.bukkit.entity.Player;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.function.Consumer;

@Service(priority = ComponentPriority.LOW)
final class BukkitFlightService implements FlightService {

    private final TaskScheduler scheduler;
    private final FlightPolicy policy;

    @Inject
    BukkitFlightService(TaskScheduler scheduler, FlightPolicy policy) {
        this.scheduler = scheduler;
        this.policy = policy;
    }

    @Override
    public void allowFlight(Player player) {
        sync(player, (p) -> p.setAllowFlight(true));
    }

    @Override
    public void disallowFlight(Player player) {
        sync(player, (p) -> p.setAllowFlight(false));
    }

    @Override
    public void stopFlying(Player player) {
        sync(player, (p) -> p.setFlying(false));
    }

    @Override
    public void refreshFlightState(Player player) {
        sync(player, (p) -> {
            boolean allowFly = policy.canFly(player.getGameMode());

            p.setFlying(false);
            p.setAllowFlight(allowFly);
        });
    }

    private void sync(Player player, Consumer<Player> action) {
        scheduler.runSyncIfNeeded(() -> action.accept(player));
    }
}
