package com.github.imdmk.doublejump.util;

import dev.rollczi.litecommands.time.DurationParser;
import dev.rollczi.litecommands.time.TemporalAmountParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for parsing and formatting {@link Duration} values.
 */
public final class DurationUtil {

    public static final TemporalAmountParser<Duration> DATE_TIME_PARSER = new DurationParser()
            .withUnit("s", ChronoUnit.SECONDS)
            .withUnit("m", ChronoUnit.MINUTES)
            .withUnit("h", ChronoUnit.HOURS)
            .withUnit("d", ChronoUnit.DAYS)
            .withUnit("w", ChronoUnit.WEEKS)
            .withUnit("mo", ChronoUnit.MONTHS)
            .withUnit("y", ChronoUnit.YEARS);

    private static final long MILLIS_PER_TICK = 50L;
    private static final long ONE_SECOND_IN_MILLIS = 1000L;

    private DurationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    /**
     * Formats the provided {@link Duration} into a human-readable string.
     * Returns {@code "<1s"} for null, zero, or negative durations.
     *
     * @param duration the duration to format
     * @return a formatted string or {@code "<1s"}
     */
    public static @NotNull String format(@NotNull Duration duration) {
        if (!isValid(duration)) {
            return "0s";
        }

        long millis = duration.toMillis();
        if (millis < ONE_SECOND_IN_MILLIS) {
            return millis + "ms";
        }

        return DATE_TIME_PARSER.format(duration);
    }

    /**
     * Checks whether the given {@link Duration} is valid (non-null, non-zero, positive).
     *
     * @param duration the duration to check
     * @return {@code true} if valid, otherwise {@code false}
     */
    public static boolean isValid(@Nullable Duration duration) {
        return duration != null && !duration.isZero() && !duration.isNegative();
    }

    /**
     * Converts a {@link Duration} to Minecraft ticks (1 tick = 50ms).
     *
     * @param duration the duration to convert
     * @return number of ticks
     */
    public static long toTicks(@NotNull Duration duration) {
        return isValid(duration) ? duration.toMillis() / MILLIS_PER_TICK : 0L;
    }

    /**
     * Converts ticks to {@link Duration}.
     * 1 tick = 50 milliseconds.
     *
     * @param ticks the number of ticks
     * @return a {@link Duration} representation
     */
    public static @NotNull Duration fromTicks(long ticks) {
        return ticks > 0 ? Duration.ofMillis(ticks * MILLIS_PER_TICK) : Duration.ZERO;
    }
}
