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

    public static TemporalAmountParser<Duration> DATE_TIME_PARSER = new DurationParser()
            .withUnit("s", ChronoUnit.SECONDS)
            .withUnit("m", ChronoUnit.MINUTES)
            .withUnit("h", ChronoUnit.HOURS)
            .withUnit("d", ChronoUnit.DAYS)
            .withUnit("w", ChronoUnit.WEEKS)
            .withUnit("mo", ChronoUnit.MONTHS)
            .withUnit("y", ChronoUnit.YEARS);

    private DurationUtil() {
        throw new UnsupportedOperationException("This is utility class.");
    }

    /**
     * Formats the provided {@link Duration} into a human-readable string.
     * <p>
     * If the duration is {@code null}, zero, or negative, the method returns {@code "<1s"}.
     * Otherwise, it formats the duration using the predefined {@link #DATE_TIME_PARSER}.
     *
     * @param duration the duration to format; must not be {@code null}
     * @return a formatted string representing the duration, or {@code "<1s"} if the duration is invalid
     */
    public static @NotNull String format(@NotNull Duration duration) {
        if (!isValid(duration)) {
            return "<1s";
        }

        return DATE_TIME_PARSER.format(duration);
    }

    /**
     * Checks whether the given {@link Duration} is valid.
     * <p>
     * A duration is considered valid if it is not {@code null} and not equal to {@link Duration#ZERO}.
     * Negative durations are considered valid as long as they are non-zero.
     *
     * @param duration the duration to check, may be {@code null}
     * @return {@code true} if the duration is non-null and not zero; {@code false} otherwise
     */
    public static boolean isValid(@Nullable final Duration duration) {
        return duration != null && !duration.isZero() && !duration.isNegative();
    }

    /**
     * Converts a duration to Minecraft ticks (1 tick = 50 ms).
     *
     * @param duration the duration to convert
     * @return duration in ticks
     */
    public static long toTicks(@NotNull final Duration duration) {
        if (duration.isZero() || duration.isNegative()) {
            return 0L;
        }

        return duration.toMillis() / 50;
    }

    /**
     * Converts Minecraft ticks to a {@link Duration}.
     * 1 tick = 50 milliseconds.
     *
     * @param ticks the number of ticks to convert
     * @return a {@link Duration} representing the given number of ticks
     */
    public static @NotNull Duration fromTicks(final long ticks) {
        if (ticks <= 0) {
            return Duration.ZERO;
        }

        return Duration.ofMillis(ticks * 50L);
    }
}
