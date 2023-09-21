package com.github.imdmk.doublejump.jump.item.listener;

import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.jump.JumpPlayerService;
import com.github.imdmk.doublejump.jump.JumpSettings;
import com.github.imdmk.doublejump.jump.event.DoubleJumpEvent;
import com.github.imdmk.doublejump.jump.item.JumpItemService;
import com.github.imdmk.doublejump.jump.item.JumpItemUsage;
import com.github.imdmk.doublejump.jump.item.JumpItemSettings;
import com.github.imdmk.doublejump.notification.Notification;
import com.github.imdmk.doublejump.notification.NotificationSender;
import com.github.imdmk.doublejump.notification.NotificationSettings;
import com.github.imdmk.doublejump.region.RegionProvider;
import com.github.imdmk.doublejump.util.DurationUtil;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class JumpItemInteractListener implements Listener {

    private final Server server;
    private final JumpSettings jumpSettings;
    private final JumpItemSettings jumpItemSettings;
    private final NotificationSettings notificationSettings;
    private final NotificationSender notificationSender;
    private final JumpPlayerManager jumpPlayerManager;
    private final JumpItemService jumpItemService;
    private final JumpPlayerService jumpPlayerService;
    private final RegionProvider regionProvider;

    public JumpItemInteractListener(Server server, JumpSettings jumpSettings, JumpItemSettings jumpItemSettings, NotificationSettings notificationSettings, NotificationSender notificationSender, JumpPlayerManager jumpPlayerManager, JumpItemService jumpItemService, JumpPlayerService jumpPlayerService, RegionProvider regionProvider) {
        this.server = server;
        this.jumpSettings = jumpSettings;
        this.jumpItemSettings = jumpItemSettings;
        this.notificationSettings = notificationSettings;
        this.notificationSender = notificationSender;
        this.jumpPlayerManager = jumpPlayerManager;
        this.jumpItemService = jumpItemService;
        this.jumpPlayerService = jumpPlayerService;
        this.regionProvider = regionProvider;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!this.jumpItemSettings.enabled) {
            return;
        }

        if (this.jumpItemSettings.usageConfiguration.usage != JumpItemUsage.CLICK_ITEM) {
            return;
        }

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack clickedItem = event.getItem();
        if (clickedItem == null) {
            return;
        }

        if (!this.jumpItemService.compare(clickedItem)) {
            return;
        }

        if (this.regionProvider.isInRegion(player)) {
            event.setCancelled(true);

            this.notificationSender.send(player, this.notificationSettings.jumpModeDisableRegionNotification);
            return;
        }

        GameMode playerGameMode = player.getGameMode();
        if (this.jumpSettings.restrictionsSettings.disabledGameModes.contains(playerGameMode)) {
            event.setCancelled(true);

            this.notificationSender.send(player, this.notificationSettings.jumpModeDisabledGameModeNotification);
            return;
        }

        String playerWorldName = player.getWorld().getName();
        if (this.jumpSettings.restrictionsSettings.disabledWorlds.contains(playerWorldName)) {
            event.setCancelled(true);

            this.notificationSender.send(player, this.notificationSettings.jumpModeDisabledWorldNotification);
            return;
        }

        if (this.jumpItemSettings.usageConfiguration.delete) {
            player.getInventory().removeItem(clickedItem);
        }

        if (this.jumpItemSettings.usageConfiguration.cancel) {
            event.setCancelled(true);
        }

        if (this.jumpItemSettings.usageConfiguration.switchDoubleJumpMode) {
            this.switchDoubleJump(player);
        }

        if (this.jumpItemSettings.usageConfiguration.doubleJump) {
            JumpPlayer jumpPlayer = this.jumpPlayerService.getOrCreateJumpPlayer(player);

            if (jumpPlayer.isDelay()) {
                Notification notification = Notification.builder()
                        .fromNotification(this.notificationSettings.jumpDelayNotification)
                        .placeholder("{TIME}", DurationUtil.toHumanReadable(jumpPlayer.getRemainingDelayDuration()))
                        .build();

                this.notificationSender.send(player, notification);
                return;
            }

            if (!jumpPlayer.hasJumps()) {
                if (this.jumpSettings.limitSettings.regenerationDelay.isZero()) {
                    this.notificationSender.send(player, this.notificationSettings.jumpLimitNotification);
                    return;
                }

                Notification jumpLimitDelayNotification = Notification.builder()
                        .fromNotification(this.notificationSettings.jumpLimitDelayNotification)
                        .placeholder("{TIME}", DurationUtil.toHumanReadable(jumpPlayer.getRemainingJumpRegenerationDuration()))
                        .build();

                this.notificationSender.send(player, jumpLimitDelayNotification);
                return;
            }

            DoubleJumpEvent doubleJumpEvent = new DoubleJumpEvent(player, jumpPlayer);

            this.server.getPluginManager().callEvent(doubleJumpEvent);
        }

        this.reduceDurability(clickedItem, this.jumpItemSettings.usageConfiguration.reduceDurability);

        if (this.jumpItemSettings.usageConfiguration.disableDoubleJumpMode) {
            this.jumpPlayerService.disable(player);
        }
    }

    private void reduceDurability(ItemStack item, int reduceBy) {
        if (reduceBy < 0) {
            return;
        }

        if (!(item.getItemMeta() instanceof Damageable itemDamageable)) {
            return;
        }

        int itemDamage = itemDamageable.getDamage();
        int itemMaxDurability = item.getType().getMaxDurability();

        boolean shouldDestroyItem = (itemDamage + reduceBy) == itemMaxDurability;

        if (shouldDestroyItem) {
            item.setAmount(0);
            return;
        }

        itemDamageable.setDamage(-(reduceBy - itemMaxDurability));
        item.setItemMeta(itemDamageable);
    }

    private void switchDoubleJump(Player player) {
        if (this.jumpPlayerManager.isDoubleJumpMode(player)) {
            this.jumpPlayerService.disable(player);

            this.notificationSender.send(player, this.notificationSettings.jumpModeDisabledNotification);
        }
        else {
            this.jumpPlayerService.enable(player, false);

            this.notificationSender.send(player, this.notificationSettings.jumpModeEnabledNotification);
        }
    }
}
