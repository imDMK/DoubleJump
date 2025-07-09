package com.github.imdmk.doublejump.infrastructure.message;

import com.eternalcode.multification.adventure.AudienceConverter;
import com.eternalcode.multification.bukkit.BukkitMultification;
import com.eternalcode.multification.notice.provider.NoticeProvider;
import com.eternalcode.multification.shared.Formatter;
import com.eternalcode.multification.translation.TranslationProvider;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Service responsible for sending formatted messages to command senders using the configured notice system.
 */
public class MessageService extends BukkitMultification<MessageConfig> {

    private final Logger logger;
    private final MessageConfig messageConfig;
    private final AudienceProvider audienceProvider;
    private final MiniMessage miniMessage;

    public MessageService(
            @NotNull Logger logger,
            @NotNull MessageConfig messageConfig,
            @NotNull AudienceProvider audienceProvider,
            @NotNull MiniMessage miniMessage
    ) {
        this.logger = Objects.requireNonNull(logger, "logger cannot be null");
        this.messageConfig = Objects.requireNonNull(messageConfig, "messageConfiguration cannot be null");
        this.audienceProvider = Objects.requireNonNull(audienceProvider, "audienceProvider cannot be null");
        this.miniMessage = Objects.requireNonNull(miniMessage, "miniMessage cannot be null");
    }

    @Override
    protected @NotNull TranslationProvider<MessageConfig> translationProvider() {
        return locale -> this.messageConfig;
    }

    @Override
    protected @NotNull ComponentSerializer<Component, Component, String> serializer() {
        return this.miniMessage;
    }

    @Override
    protected @NotNull AudienceConverter<CommandSender> audienceConverter() {
        return commandSender -> {
            if (commandSender instanceof Player player) {
                return this.audienceProvider.player(player.getUniqueId());
            }

            return this.audienceProvider.console();
        };
    }

    /**
     * Sends a predefined notice message to the given sender.
     *
     * @param sender the target recipient, must not be {@code null}
     * @param notice the notice to send, must not be {@code null}
     */
    public void send(@NotNull CommandSender sender, @NotNull NoticeProvider<MessageConfig> notice) {
        this.create().viewer(sender).notice(notice).send();
    }

    /**
     * Sends a predefined notice message to the given sender with formatting support.
     *
     * @param sender    the target recipient, must not be {@code null}
     * @param notice    the notice to send, must not be {@code null}
     * @param formatter the formatter used to replace placeholders, must not be {@code null}
     */
    public void send(@NotNull CommandSender sender, @NotNull NoticeProvider<MessageConfig> notice, @NotNull Formatter formatter) {
        this.create()
                .viewer(sender)
                .notice(notice)
                .formatter(formatter)
                .send();
    }

    /**
     * Closes underlying resources used by this service (e.g., audiences).
     */
    public void close() {
        this.logger.info("Closing MessageService");
        this.audienceProvider.close();
    }
}
