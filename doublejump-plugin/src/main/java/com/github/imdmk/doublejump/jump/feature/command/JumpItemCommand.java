package com.github.imdmk.doublejump.jump.feature.command;

import com.github.imdmk.doublejump.infrastructure.message.MessageService;
import com.github.imdmk.doublejump.jump.feature.item.JumpItemService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.panda_lang.utilities.inject.annotations.Inject;

@Command(name = "doublejump item")
@Permission("command.doublejump.item")
public class JumpItemCommand {

    @Inject private MessageService messageService;
    @Inject private JumpItemService jumpItemService;

    @Execute(name = "give")
    void give(@Context CommandSender sender, @Arg Player target) {
        target.getInventory().addItem(this.jumpItemService.getJumpItem().asItemStack());
        this.messageService.send(sender, notice -> notice.jumpItemAdded);
    }

    @Execute(name = "remove")
    void remove(@Context CommandSender sender, @Arg Player target) {
        if (!this.hasJumpItem(target)) {
            this.messageService.send(sender, notice -> notice.jumpItemNotFound);
            return;
        }

        target.getInventory().removeItem(this.jumpItemService.getJumpItem().asItemStack());
        this.messageService.send(sender, notice -> notice.jumpItemRemovedSingle);
    }

    @Execute(name = "remove-all")
    void removeAll(@Context CommandSender sender, @Arg Player target) {
        if (!this.hasJumpItem(target)) {
            this.messageService.send(sender, notice -> notice.jumpItemNotFound);
            return;
        }

        for (ItemStack item : target.getInventory().getContents()) {
            if (this.jumpItemService.isJumpItem(item)) {
                target.getInventory().removeItem(item);
            }
        }

        this.messageService.send(sender, notice -> notice.jumpItemRemovedAll);
    }

    private boolean hasJumpItem(@NotNull Player player) {
        return player.getInventory().contains(this.jumpItemService.getJumpItem().asItemStack());
    }
}
