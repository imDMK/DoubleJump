package com.github.imdmk.doublejump.util;

import java.time.Duration;

public final class DurationUtil {

    private DurationUtil() {
        throw new UnsupportedOperationException("This is utility class.");
    }

    public static String toHumanReadable(Duration duration) {
        return Duration.ofSeconds(duration.toSeconds()) //We want to remove millis seconds
                .toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }

    public static long toTicks(Duration duration) {
        return duration.toMillis() / 50L;
    }
}
