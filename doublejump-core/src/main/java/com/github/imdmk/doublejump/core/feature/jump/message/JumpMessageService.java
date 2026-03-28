package com.github.imdmk.doublejump.core.feature.jump.message;

import com.github.imdmk.doublejump.core.feature.jump.rule.JumpResult;
import com.github.imdmk.doublejump.core.injector.ComponentPriority;
import com.github.imdmk.doublejump.core.injector.annotations.Service;
import com.github.imdmk.doublejump.core.message.MessageService;
import org.bukkit.entity.Player;
import org.panda_lang.utilities.inject.annotations.Inject;

@Service(priority = ComponentPriority.LOW, order = 2)
public final class JumpMessageService {

    private final MessageService messageService;
    private final JumpMessageResolver noticeResolver;
    private final JumpMessageFormatter formatterProvider;

    @Inject
    JumpMessageService(
            MessageService messageService,
            JumpMessageResolver noticeResolver,
            JumpMessageFormatter formatterProvider
    ) {
        this.messageService = messageService;
        this.noticeResolver = noticeResolver;
        this.formatterProvider = formatterProvider;
    }

    public void notify(Player player, JumpResult result) {
        messageService.create()
                .viewer(player)
                .notice(noticeResolver.resolve(result))
                .formatter(formatterProvider.create(player, result))
                .send();
    }
}
