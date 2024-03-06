package com.github.imdmk.doublejump.command.context;

import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerContext implements ContextProvider<CommandSender, Player> {

    @Override
    public ContextResult<Player> provide(Invocation<CommandSender> invocation) {
        if (invocation.sender() instanceof Player player) {
            return ContextResult.ok(() -> player);
        }

        return ContextResult.error("Only player can use this command.");
    }
}
