package com.github.imdmk.doublejump.jump.item.command;

import com.github.imdmk.doublejump.jump.item.JumpItemSettings;
import com.github.imdmk.doublejump.notification.NotificationSender;
import com.github.imdmk.doublejump.text.Formatter;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Command(name = "doublejump item")
@Permission("command.doublejump.item")
public class DoubleJumpItemCommand {

    private final JumpItemSettings jumpItemSettings;
    private final NotificationSender notificationSender;

    public DoubleJumpItemCommand(JumpItemSettings jumpItemSettings, NotificationSender notificationSender) {
        this.jumpItemSettings = jumpItemSettings;
        this.notificationSender = notificationSender;
    }

    @Execute(name = "give")
    void giveItem(@Context CommandSender sender, @Arg("target") Player target) {
        if (!this.jumpItemSettings.enabled) {
            this.notificationSender.send(sender, this.jumpItemSettings.notificationSettings.jumpItemDisabled);
            return;
        }

        ItemStack jumpItem = this.jumpItemSettings.item;
        Inventory targetInventory = target.getInventory();

        if (targetInventory.firstEmpty() == -1) {
            this.notificationSender.send(sender, this.jumpItemSettings.notificationSettings.targetHasFullInventory);
            return;
        }

        targetInventory.addItem(jumpItem);

        Formatter formatter = new Formatter()
                .placeholder("{PLAYER}", target.getName());

        this.notificationSender.send(sender, this.jumpItemSettings.notificationSettings.jumpItemAdded, formatter);
    }

    @Execute(name = "remove")
    void removeItem(@Context CommandSender sender, @Arg("target") Player target) {
        ItemStack jumpItem = this.jumpItemSettings.item;

        Inventory targetInventory = target.getInventory();
        Inventory targetEnderChest = target.getEnderChest();

        if (!targetInventory.contains(jumpItem) && !targetEnderChest.contains(jumpItem)) {
            this.notificationSender.send(sender, this.jumpItemSettings.notificationSettings.targetHasNoJumpItem);
            return;
        }

        targetInventory.remove(jumpItem);
        targetEnderChest.remove(jumpItem);

        Formatter formatter = new Formatter()
                .placeholder("{PLAYER}", target.getName());

        this.notificationSender.send(sender, this.jumpItemSettings.notificationSettings.jumpItemRemoved, formatter);
    }
}
