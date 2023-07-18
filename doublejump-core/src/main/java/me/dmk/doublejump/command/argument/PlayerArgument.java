package me.dmk.doublejump.command.argument;

import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import me.dmk.doublejump.configuration.PluginConfiguration;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import panda.std.Result;

import java.util.List;
import java.util.Optional;

public class PlayerArgument implements OneArgument<Player> {

    private final Server server;
    private final PluginConfiguration pluginConfiguration;

    public PlayerArgument(Server server, PluginConfiguration pluginConfiguration) {
        this.server = server;
        this.pluginConfiguration = pluginConfiguration;
    }

    @Override
    public Result<Player, ?> parse(LiteInvocation invocation, String argument) {
        Optional<Player> playerOptional = Optional.ofNullable(this.server.getPlayerExact(argument));

        return playerOptional.map(Result::ok)
                .orElseGet(() -> Result.error(this.pluginConfiguration.playerNotFoundNotification));
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return this.server.getOnlinePlayers().stream()
                .map(Player::getName)
                .map(Suggestion::of)
                .toList();
    }
}
