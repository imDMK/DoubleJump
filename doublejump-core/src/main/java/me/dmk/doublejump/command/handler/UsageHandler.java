package me.dmk.doublejump.command.handler;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.InvalidUsageHandler;
import dev.rollczi.litecommands.schematic.Schematic;
import me.dmk.doublejump.configuration.PluginConfiguration;
import me.dmk.doublejump.notification.Notification;
import me.dmk.doublejump.notification.NotificationSender;
import org.bukkit.command.CommandSender;

public class UsageHandler implements InvalidUsageHandler<CommandSender> {

    private final PluginConfiguration pluginConfiguration;
    private final NotificationSender notificationSender;

    public UsageHandler(PluginConfiguration pluginConfiguration, NotificationSender notificationSender) {
        this.pluginConfiguration = pluginConfiguration;
        this.notificationSender = notificationSender;
    }

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, Schematic schematic) {
        if (schematic.isOnlyFirst()) {
            Notification notification = Notification.builder()
                    .fromNotification(this.pluginConfiguration.invalidUsageNotification)
                    .placeholder("{USAGE}", schematic.first())
                    .build();

            this.notificationSender.sendMessage(sender, notification);
            return;
        }

        this.notificationSender.sendMessage(sender, this.pluginConfiguration.invalidUsageFirstNotification);

        for (String schema : schematic.getSchematics()) {
            Notification notification = Notification.builder()
                    .fromNotification(this.pluginConfiguration.invalidUsageListNotification)
                    .placeholder("{USAGE}", schema)
                    .build();

            this.notificationSender.sendMessage(sender, notification);
        }
    }
}
