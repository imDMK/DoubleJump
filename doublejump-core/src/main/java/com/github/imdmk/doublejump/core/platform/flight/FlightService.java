package com.github.imdmk.doublejump.core.platform.flight;

import org.bukkit.entity.Player;

public interface FlightService {

    void allowFlight(Player player);
    void disallowFlight(Player player);

    void stopFlying(Player player);

    void refreshFlightState(Player player);

}
