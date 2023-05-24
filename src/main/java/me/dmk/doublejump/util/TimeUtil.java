package me.dmk.doublejump.util;

import java.time.Duration;
import java.time.Instant;

public class TimeUtil {

    private TimeUtil() {
        throw new UnsupportedOperationException("This is utility class.");
    }

    public static String instantToString(Instant instant) {
        Duration duration = Duration.between(Instant.now(), instant);

        if (duration.isNegative() || duration.isZero()) {
            return "<1s";
        }

        long millis = duration.toMillis();
        long seconds = duration.toSecondsPart();
        long minutes = duration.toMinutesPart();

        StringBuilder stringBuilder = new StringBuilder();

        if (minutes > 0) {
            stringBuilder
                    .append(minutes)
                    .append("m")
                    .append(", ");
        }

        if (seconds > 0) {
            stringBuilder
                    .append(seconds)
                    .append("s");
        }

        if (stringBuilder.isEmpty()) {
            if (millis > 0) {
                stringBuilder
                        .append(millis)
                        .append("ms");
            }
            else {
                stringBuilder
                        .append("<1s");
            }
        }

        return stringBuilder.toString();
    }
}
