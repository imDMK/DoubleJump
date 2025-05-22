package com.github.imdmk.doublejump.util;

import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Set;

/**
 * Utility class for {@link GameMode}-related operations.
 * <p>
 * This class provides helper methods to work with {@link GameMode} enums,
 * specifically to identify game modes that allow flying.
 * </p>
 *
 * <p>This class is not meant to be instantiated.</p>
 */
public final class GameModeUtil {

    private static final Set<GameMode> FLYING_GAME_MODES = EnumSet.of(GameMode.SPECTATOR, GameMode.CREATIVE);

    private GameModeUtil() {
        throw new UnsupportedOperationException("This is utility class.");
    }

    /**
     * Checks if the given {@link GameMode} allows flying.
     *
     * @param gameMode the game mode to check; must not be null
     * @return {@code true} if the game mode allows flying (e.g., CREATIVE or SPECTATOR),
     *         {@code false} otherwise
     */
    public static boolean isFlyingGameMode(@NotNull GameMode gameMode) {
        return FLYING_GAME_MODES.contains(gameMode);
    }
}
