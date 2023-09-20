package com.github.imdmk.doublejump.util;

import org.bukkit.GameMode;

public class GameModeUtil {

    private GameModeUtil() {
        throw new UnsupportedOperationException("This is utility class.");
    }

    /**
     * Checks if the game mode is allowed to fly.
     *
     * @param gameMode Game mode to check
     * @return Whether the game mode has permission to fly
     */
    public static boolean isGameModeCanFly(GameMode gameMode) {
        return gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE;
    }
}
