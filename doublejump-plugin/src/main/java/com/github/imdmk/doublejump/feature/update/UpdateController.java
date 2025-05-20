package com.github.imdmk.doublejump.feature.update;

import com.eternalcode.gitcheck.GitCheckResult;
import com.eternalcode.gitcheck.git.GitException;
import com.eternalcode.multification.notice.Notice;
import com.github.imdmk.doublejump.configuration.PluginConfiguration;
import com.github.imdmk.doublejump.feature.message.MessageService;
import com.github.imdmk.doublejump.task.TaskScheduler;
import com.github.imdmk.doublejump.util.DurationUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateController implements Listener {

    private static final String PREFIX = "<dark_gray>[<rainbow>DoubleJump<dark_gray>] ";
    private static final Notice UPDATE_AVAILABLE = Notice.chat(
            " ",
            PREFIX + "<rainbow>A new update is available!",
            "<dark_gray>- <rainbow>We strongly recommend downloading it!",
            " "
    );
    private static final Notice UPDATE_EXCEPTION = Notice.chat(
            " ",
            PREFIX + "<red>An error occurred while checking for plugin update! Next update check: {UPDATE_CHECK_INTERVAL}",
            " "
    );

    @Inject private Logger logger;
    @Inject private PluginConfiguration pluginConfiguration;
    @Inject private MessageService messageService;
    @Inject private UpdateService updateService;
    @Inject private TaskScheduler taskScheduler;

    @EventHandler(priority = EventPriority.HIGHEST)
    void onPlayerJoin(PlayerJoinEvent event) {
        if (!this.pluginConfiguration.checkUpdate) {
            return;
        }

        Player player = event.getPlayer();

        if (!player.isOp()) {
            return;
        }

        if (this.updateService.shouldCheck()) {
            this.taskScheduler.runAsync(() -> this.checkForUpdate(player));
        }
    }

    private void checkForUpdate(Player player) {
        try {
            GitCheckResult result = this.updateService.check();
            if (result.isUpToDate()) {
                return;
            }

            this.sendNotice(player, UPDATE_AVAILABLE);
        }
        catch (GitException exception) {
            this.logger.log(Level.SEVERE, "An error occurred while checking for update", exception);
            this.sendNotice(player, UPDATE_EXCEPTION);
        }
    }

    private void sendNotice(Player player, Notice notice) {
        this.messageService.create()
                .notice(notice)
                .placeholder("{UPDATE_CHECK_INTERVAL}", DurationUtil.format(this.pluginConfiguration.updateInterval))
                .viewer(player)
                .send();
    }
}
