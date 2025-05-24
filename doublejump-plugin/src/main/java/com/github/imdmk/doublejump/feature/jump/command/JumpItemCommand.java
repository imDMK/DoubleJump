package com.github.imdmk.doublejump.feature.jump.command;

import com.github.imdmk.doublejump.feature.jump.item.JumpItemService;
import com.github.imdmk.doublejump.feature.message.MessageService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Command(name = "doublejump item")
public class JumpItemCommand {

    private final MessageService messageService;
    private final JumpItemService jumpItemService;

    public JumpItemCommand(
            @NotNull MessageService messageService,
            @NotNull JumpItemService jumpItemService
    ) {
        this.messageService = Objects.requireNonNull(messageService, "messageService cannot be null");
        this.jumpItemService = Objects.requireNonNull(jumpItemService, "jumpItemService cannot be null");
    }

    @Execute(name = "give")
    void give(@Context CommandSender sender, @Arg Player target) {
        target.getInventory().addItem(this.jumpItemService.getJumpItem());
        this.messageService.send(sender, notice -> notice.jumpItemAdded);
    }

    @Execute(name = "remove")
    void remove(@Context CommandSender sender, @Arg Player target) {
        if (!this.hasJumpItem(target)) {
            this.messageService.send(sender, notice -> notice.jumpItemNotFound);
            return;
        }

        target.getInventory().removeItem(this.jumpItemService.getJumpItem());

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
        return player.getInventory().contains(this.jumpItemService.getJumpItem());
    }
}
