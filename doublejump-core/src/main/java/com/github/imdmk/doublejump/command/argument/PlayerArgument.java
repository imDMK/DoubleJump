package com.github.imdmk.doublejump.command.argument;

import com.github.imdmk.doublejump.notification.configuration.NotificationSettings;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import panda.std.Result;

import java.util.List;
import java.util.Optional;

public class PlayerArgument implements OneArgument<Player> {

    private final Server server;
    private final NotificationSettings notificationSettings;

    public PlayerArgument(Server server, NotificationSettings notificationSettings) {
        this.server = server;
        this.notificationSettings = notificationSettings;
    }

    @Override
    public Result<Player, ?> parse(LiteInvocation invocation, String argument) {
        Optional<Player> playerOptional = Optional.ofNullable(this.server.getPlayerExact(argument));

        return playerOptional.map(Result::ok)
                .orElseGet(() -> Result.error(this.notificationSettings.playerNotFound));
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return this.server.getOnlinePlayers().stream()
                .map(Player::getName)
                .map(Suggestion::of)
                .toList();
    }
}
