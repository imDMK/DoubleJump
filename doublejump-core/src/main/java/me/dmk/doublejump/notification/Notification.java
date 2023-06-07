package me.dmk.doublejump.notification;

import eu.okaeri.configs.OkaeriConfig;

public class Notification extends OkaeriConfig {

    private NotificationType type;
    private String message;

    public Notification(NotificationType notificationType, String message) {
        this.type = notificationType;
        this.message = message;
    }

    public NotificationType getType() {
        return this.type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
