package com.github.imdmk.doublejump.core.feature.jump.message;

import com.eternalcode.multification.shared.Formatter;
import com.github.imdmk.doublejump.core.feature.jump.cooldown.JumpCooldownService;
import com.github.imdmk.doublejump.core.feature.jump.rule.JumpResult;
import com.github.imdmk.doublejump.core.injector.ComponentPriority;
import com.github.imdmk.doublejump.core.injector.annotations.Service;
import com.github.imdmk.doublejump.core.shared.time.TimeFormatter;
import org.bukkit.entity.Player;
import org.panda_lang.utilities.inject.annotations.Inject;

@Service(priority = ComponentPriority.LOW)
final class JumpMessageFormatter {

    private final JumpCooldownService cooldownService;
    private final TimeFormatter timeFormatter;

    @Inject
    JumpMessageFormatter(
            JumpCooldownService cooldownService,
            TimeFormatter timeFormatter
    ) {
        this.cooldownService = cooldownService;
        this.timeFormatter = timeFormatter;
    }

    public Formatter create(Player player, JumpResult result) {
        Formatter formatter = new Formatter();

        if (result == JumpResult.COOLDOWN) {
            long cooldown = cooldownService.getRemainingMillis(player.getUniqueId());
            formatter.register("{COOLDOWN}", timeFormatter.format(cooldown));
        }

        return formatter;
    }
}
