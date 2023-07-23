package me.dmk.doublejump.command;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import me.dmk.doublejump.configuration.JumpConfiguration;
import me.dmk.doublejump.configuration.MessageConfiguration;
import me.dmk.doublejump.notification.Notification;
import me.dmk.doublejump.notification.NotificationSender;
import me.dmk.doublejump.player.JumpPlayerManager;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Route(name = "doublejump")
public class DoubleJumpCommand {

    private final JumpConfiguration jumpConfiguration;
    private final MessageConfiguration messageConfiguration;
    private final NotificationSender notificationSender;
    private final JumpPlayerManager jumpPlayerManager;

    public DoubleJumpCommand(JumpConfiguration jumpConfiguration, MessageConfiguration messageConfiguration, NotificationSender notificationSender, JumpPlayerManager jumpPlayerManager) {
        this.jumpConfiguration = jumpConfiguration;
        this.messageConfiguration = messageConfiguration;
        this.notificationSender = notificationSender;
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @Async
    @Execute(required = 0)
    void execute(Player player) {
        GameMode playerGameMode = player.getGameMode();
        String playerWorldName = player.getWorld().getName();

        if (this.jumpConfiguration.disabledGameModes.contains(playerGameMode)) {
            this.notificationSender.sendMessage(player, this.messageConfiguration.jumpModeDisabledGameModeNotification);
            return;
        }

        if (this.jumpConfiguration.disabledWorlds.contains(playerWorldName)) {
            this.notificationSender.sendMessage(player, this.messageConfiguration.jumpModeDisabledWorldNotification);
            return;
        }

        if (this.jumpPlayerManager.isDoubleJumpMode(player)) {
            this.jumpPlayerManager.disable(player);
            this.notificationSender.sendMessage(player, this.messageConfiguration.jumpModeDisabledNotification);
        }
        else {
            this.jumpPlayerManager.enable(player, true);
            this.notificationSender.sendMessage(player, this.messageConfiguration.jumpModeEnabledNotification);
        }
    }

    @Async
    @Execute(route = "enable-for", required = 1)
    void enableFor(Player player, @Arg @Name("target") Player target) {
        GameMode targetGameMode = target.getGameMode();
        String targetWorldName = target.getWorld().getName();

        if (this.jumpConfiguration.disabledGameModes.contains(targetGameMode)) {
            this.notificationSender.sendMessage(player, this.messageConfiguration.targetHasDisabledGameModeNotification);
            return;
        }

        if (this.jumpConfiguration.disabledWorlds.contains(targetWorldName)) {
            this.notificationSender.sendMessage(player, this.messageConfiguration.targetInDisabledWorldNotification);
            return;
        }

        this.jumpPlayerManager.enable(player, true);

        Notification notification = Notification.builder()
                .fromNotification(this.messageConfiguration.jumpModeEnabledForNotification)
                .placeholder("{PLAYER}", target.getName())
                .build();

        this.notificationSender.sendMessage(player, notification);
    }

    @Async
    @Execute(route = "disable-for", required = 1)
    void disableFor(Player player, @Arg @Name("target") Player target) {
        this.jumpPlayerManager.disable(target);

        Notification notification = Notification.builder()
                .fromNotification(this.messageConfiguration.jumpModeDisabledForNotification)
                .placeholder("{PLAYER}", target.getName())
                .build();

        this.notificationSender.sendMessage(player, notification);
    }

    @Async
    @Execute(route = "give-item", required = 1)
    void giveItem(CommandSender sender, @Arg @Name("target") Player target) {
        if (!this.jumpConfiguration.jumpItemEnabled) {
            this.notificationSender.sendMessage(sender, this.messageConfiguration.jumpItemDisabledNotification);
            return;
        }

        ItemStack jumpItem = this.jumpConfiguration.jumpItem;
        Inventory targetInventory = target.getInventory();

        if (targetInventory.firstEmpty() == -1) {
            this.notificationSender.sendMessage(sender, this.messageConfiguration.targetFullInventoryNotification);
            return;
        }

        targetInventory.addItem(jumpItem);

        Notification notification = Notification.builder()
                .fromNotification(this.messageConfiguration.jumpItemAddedNotification)
                .placeholder("{PLAYER}", target.getName())
                .build();

        this.notificationSender.sendMessage(sender, notification);
    }

    @Async
    @Execute(route = "remove-item", required = 1)
    void removeItem(CommandSender sender, @Arg @Name("target") Player target) {
        ItemStack jumpItem = this.jumpConfiguration.jumpItem;

        Inventory targetInventory = target.getInventory();
        Inventory targetEnderChest = target.getEnderChest();

        if (!targetInventory.contains(jumpItem) && !targetEnderChest.contains(jumpItem)) {
            this.notificationSender.sendMessage(sender, this.messageConfiguration.targetHasNoJumpItemNotification);
            return;
        }

        targetInventory.remove(jumpItem);
        targetEnderChest.remove(jumpItem);

        Notification notification = Notification.builder()
                .fromNotification(this.messageConfiguration.jumpItemRemovedNotification)
                .placeholder("{PLAYER}", target.getName())
                .build();

        this.notificationSender.sendMessage(sender, notification);
    }
}
