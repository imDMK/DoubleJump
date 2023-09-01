package com.github.imdmk.doublejump.util;

import java.time.Duration;

public final class DurationUtil {

    private DurationUtil() {
        throw new UnsupportedOperationException("This is utility class.");
    }

    public static String toHumanReadable(Duration duration) {
        Duration ofSeconds = Duration.ofSeconds(duration.toSeconds()); //We want to remove milliseconds

        if (ofSeconds.isNegative() || duration.isZero()) {
            return "<1s";
        }

        return ofSeconds
                .toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }

    public static long toTicks(Duration duration) {
        return duration.toMillis() / 50L;
    }
}
