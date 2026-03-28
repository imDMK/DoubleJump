package com.github.imdmk.doublejump.core.shared.time;

import com.github.imdmk.doublejump.core.injector.ComponentPriority;
import com.github.imdmk.doublejump.core.injector.annotations.Service;
import org.panda_lang.utilities.inject.annotations.Inject;

@Service(priority = ComponentPriority.LOWEST, order = 2)
public final class TimeFormatter {

    private final TimeFormatConfig config;

    @Inject
    TimeFormatter(TimeFormatConfig config) {
        this.config = config;
    }

    public String format(long millis) {
        if (millis <= 0) {
            return "0.0" + config.seconds;
        }

        long totalSeconds = millis / 1000;
        if (totalSeconds < 60) {
            return formatSeconds(millis);
        }

        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        if (minutes < 60) {
            return formatMinutes(minutes, seconds);
        }
        long hours = minutes / 60;
        long remainingMinutes = minutes % 60;

        return formatHours(hours, remainingMinutes);
    }

    private String formatSeconds(long millis) {
        long seconds = millis / 1000;
        long decimal = (millis % 1000) / 100;
        String suffix = config.seconds;

        return seconds + "." + decimal + suffix;
    }

    private String formatMinutes(long minutes, long seconds) {
        String minutesSuffix = config.minutes;
        String secondsSuffix = config.seconds;
        if (seconds == 0) {
            return minutes + minutesSuffix;
        }

        return minutes + minutesSuffix + " " + seconds + secondsSuffix;
    }

    private String formatHours(long hours, long minutes) {
        String hoursSuffix = config.hours;
        String minutesSuffix = config.minutes;
        if (minutes == 0) {
            return hours + hoursSuffix;
        }

        return hours + hoursSuffix + " " + minutes + minutesSuffix;
    }
}