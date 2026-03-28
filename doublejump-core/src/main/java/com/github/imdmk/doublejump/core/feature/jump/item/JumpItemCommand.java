package com.github.imdmk.doublejump.core.feature.jump.item;

import com.github.imdmk.doublejump.core.injector.annotations.lite.LiteCommand;
import com.github.imdmk.doublejump.core.message.MessageService;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.panda_lang.utilities.inject.annotations.Inject;

@LiteCommand
@Command(name = "doublejump item")
@Permission("command.doublejump.item")
final class JumpItemCommand {

    private final MessageService messageService;
    private final JumpItemService itemService;

    @Inject
    JumpItemCommand(
            MessageService messageService,
            JumpItemService itemService
    ) {
        this.messageService = messageService;
        this.itemService = itemService;
    }

    @Execute(name = "give")
    void giveItem(@Context CommandSender sender, @Arg Player target) {
        target.getInventory().addItem(itemService.getItem().toItemStack());

        messageService.send(target, n -> n.jumpMessages.itemGive());
        messageService.create()
                .viewer(sender)
                .notice(n -> n.jumpMessages.itemGiveToTarget())
                .placeholder("{PLAYER}", target.getName())
                .send();
    }

    @Execute(name = "remove")
    void removeItem(@Context CommandSender sender, @Arg Player target) {
        target.getInventory().remove(itemService.getItem().toItemStack());

        messageService.send(target, n -> n.jumpMessages.itemRemove());
        messageService.create()
                .viewer(sender)
                .notice(n -> n.jumpMessages.itemRemoveFromTarget())
                .placeholder("{PLAYER}", target.getName())
                .send();
    }
}
