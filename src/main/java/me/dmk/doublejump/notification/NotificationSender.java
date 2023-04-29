package me.dmk.doublejump.notification;

import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

/**
 * Notification sender with adventure support
 */
public class NotificationSender {

    private final AudienceProvider audienceProvider;
    private final MiniMessage miniMessage;

    public NotificationSender(AudienceProvider audienceProvider, MiniMessage miniMessage) {
        this.audienceProvider = audienceProvider;
        this.miniMessage = miniMessage;
    }

    /**
     * Sends a component
     * @param player The player to send to
     * @param message The message to send
     */
    public void sendMessage(Player player, String message) {
        if (message.isEmpty() || message.isBlank()) {
            return;
        }

        Component component = this.miniMessage.deserialize(message);

        this.audienceProvider
                .player(player.getUniqueId())
                .sendMessage(component);
    }

    /**
     * Sends a notification
     * @param player The player to send to
     * @param notification The notification to send
     */
    public void sendMessage(Player player, Notification notification) {
        NotificationType type = notification.getType();
        String message = notification.getMessage();

        if (message.isEmpty() || message.isBlank()) {
            return;
        }

        switch (type) {
            case CHAT -> this.sendMessage(player, message);
            case ACTIONBAR -> this.sendActionBar(player, message);
            case TITLE -> this.sendTitle(player, message, "");
            case SUBTITLE -> this.sendTitle(player, "", message);
        }
    }

    /**
     * Sends a notification
     * @param player The player to send to
     * @param notificationType notification type
     * @param message The message to send
     */
    public void sendMessage(Player player, NotificationType notificationType, String message) {
        if (message.isEmpty() || message.isBlank()) {
            return;
        }

        switch (notificationType) {
            case CHAT -> this.sendMessage(player, message);
            case ACTIONBAR -> this.sendActionBar(player, message);
            case TITLE -> this.sendTitle(player, message, "");
            case SUBTITLE -> this.sendTitle(player, "", message);
        }
    }

    /**
     * Sends an actionbar message
     * @param player The player to send to
     * @param message The message to send
     */
    public void sendActionBar(Player player, String message) {
        Component component = this.miniMessage.deserialize(message);

        this.audienceProvider
                .player(player.getUniqueId())
                .sendActionBar(component);
    }

    /**
     * Sends an title
     * @param player The player to send to
     * @param title The title message to send
     * @param subtitle The subtitle message to send
     */
    public void sendTitle(Player player, String title, String subtitle) {
        Title titleMessage = Title.title(
                this.miniMessage.deserialize(title),
                this.miniMessage.deserialize(subtitle),
                Title.DEFAULT_TIMES
        );

        this.audienceProvider
                .player(player.getUniqueId())
                .showTitle(titleMessage);
    }
}
