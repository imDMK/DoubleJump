package com.github.imdmk.doublejump.command.argument;

import com.github.imdmk.doublejump.notification.configuration.NotificationSettings;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PlayerArgument extends ArgumentResolver<CommandSender, Player> {

    private final Server server;
    private final NotificationSettings notificationSettings;

    public PlayerArgument(Server server, NotificationSettings notificationSettings) {
        this.server = server;
        this.notificationSettings = notificationSettings;
    }

    @Override
    protected ParseResult<Player> parse(Invocation<CommandSender> invocation, Argument<Player> context, String argument) {
        return Optional.ofNullable(this.server.getPlayerExact(argument))
                .map(ParseResult::success)
                .orElseGet(() -> ParseResult.failure(this.notificationSettings.playerNotFound));
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Player> argument, SuggestionContext context) {
        return this.server.getOnlinePlayers().stream()
                .map(HumanEntity::getName)
                .collect(SuggestionResult.collector());
    }
}
