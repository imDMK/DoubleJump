package com.github.imdmk.doublejump.jump.item.listener;

import com.github.imdmk.doublejump.configuration.MessageConfiguration;
import com.github.imdmk.doublejump.jump.JumpConfiguration;
import com.github.imdmk.doublejump.jump.JumpPlayer;
import com.github.imdmk.doublejump.jump.JumpPlayerManager;
import com.github.imdmk.doublejump.jump.event.DoubleJumpEvent;
import com.github.imdmk.doublejump.jump.item.JumpItemService;
import com.github.imdmk.doublejump.jump.item.JumpItemUsage;
import com.github.imdmk.doublejump.jump.item.configuration.JumpItemConfiguration;
import com.github.imdmk.doublejump.notification.Notification;
import com.github.imdmk.doublejump.notification.NotificationSender;
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
    private final JumpConfiguration jumpConfiguration;
    private final JumpItemConfiguration jumpItemConfiguration;
    private final MessageConfiguration messageConfiguration;
    private final NotificationSender notificationSender;
    private final JumpItemService jumpItemService;
    private final JumpPlayerManager jumpPlayerManager;
    private final RegionProvider regionProvider;

    public JumpItemInteractListener(Server server, JumpConfiguration jumpConfiguration, JumpItemConfiguration jumpItemConfiguration, MessageConfiguration messageConfiguration, NotificationSender notificationSender, JumpItemService jumpItemService, JumpPlayerManager jumpPlayerManager, RegionProvider regionProvider) {
        this.server = server;
        this.jumpConfiguration = jumpConfiguration;
        this.jumpItemConfiguration = jumpItemConfiguration;
        this.messageConfiguration = messageConfiguration;
        this.notificationSender = notificationSender;
        this.jumpItemService = jumpItemService;
        this.jumpPlayerManager = jumpPlayerManager;
        this.regionProvider = regionProvider;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!this.jumpItemConfiguration.enabled) {
            return;
        }

        if (this.jumpItemConfiguration.usageConfiguration.usage != JumpItemUsage.CLICK_ITEM) {
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

            this.notificationSender.sendMessage(player, this.messageConfiguration.jumpModeDisableRegionNotification);
            return;
        }

        GameMode playerGameMode = player.getGameMode();
        if (this.jumpConfiguration.restrictionsConfiguration.disabledGameModes.contains(playerGameMode)) {
            event.setCancelled(true);

            this.notificationSender.sendMessage(player, this.messageConfiguration.jumpModeDisabledGameModeNotification);
            return;
        }

        String playerWorldName = player.getWorld().getName();
        if (this.jumpConfiguration.restrictionsConfiguration.disabledWorlds.contains(playerWorldName)) {
            event.setCancelled(true);

            this.notificationSender.sendMessage(player, this.messageConfiguration.jumpModeDisabledWorldNotification);
            return;
        }

        if (this.jumpItemConfiguration.usageConfiguration.delete) {
            player.getInventory().removeItem(clickedItem);
        }

        if (this.jumpItemConfiguration.usageConfiguration.cancel) {
            event.setCancelled(true);
        }

        if (this.jumpItemConfiguration.usageConfiguration.switchDoubleJumpMode) {
            this.switchDoubleJump(player);
        }

        if (this.jumpItemConfiguration.usageConfiguration.doubleJump) {
            JumpPlayer jumpPlayer = this.jumpPlayerManager.getOrCreateJumpPlayer(player);

            if (jumpPlayer.isDelay()) {
                Notification notification = Notification.builder()
                        .fromNotification(this.messageConfiguration.jumpDelayNotification)
                        .placeholder("{TIME}", DurationUtil.toHumanReadable(jumpPlayer.getRemainingDelayDuration()))
                        .build();

                this.notificationSender.sendMessage(player, notification);
                return;
            }

            if (!jumpPlayer.hasJumps()) {
                if (this.jumpConfiguration.limitConfiguration.regenerationDelay.isZero()) {
                    this.notificationSender.sendMessage(player, this.messageConfiguration.jumpLimitNotification);
                    return;
                }

                Notification jumpLimitDelayNotification = Notification.builder()
                        .fromNotification(this.messageConfiguration.jumpLimitDelayNotification)
                        .placeholder("{TIME}", DurationUtil.toHumanReadable(jumpPlayer.getRemainingJumpRegenerationDuration()))
                        .build();

                this.notificationSender.sendMessage(player, jumpLimitDelayNotification);
                return;
            }

            DoubleJumpEvent doubleJumpEvent = new DoubleJumpEvent(player, jumpPlayer);

            this.server.getPluginManager().callEvent(doubleJumpEvent);
        }

        this.reduceDurability(clickedItem, this.jumpItemConfiguration.usageConfiguration.reduceDurability);

        if (this.jumpItemConfiguration.usageConfiguration.disableDoubleJumpMode) {
            this.jumpPlayerManager.disable(player);
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
            this.jumpPlayerManager.disable(player);

            this.notificationSender.sendMessage(player, this.messageConfiguration.jumpModeDisabledNotification);
        }
        else {
            this.jumpPlayerManager.enable(player, false);

            this.notificationSender.sendMessage(player, this.messageConfiguration.jumpModeEnabledNotification);
        }
    }
}
