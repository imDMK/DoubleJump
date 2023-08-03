package me.dmk.doublejump.listener.item;

import me.dmk.doublejump.configuration.JumpConfiguration;
import me.dmk.doublejump.configuration.JumpItemUsage;
import me.dmk.doublejump.configuration.MessageConfiguration;
import me.dmk.doublejump.event.DoubleJumpEvent;
import me.dmk.doublejump.notification.Notification;
import me.dmk.doublejump.notification.NotificationSender;
import me.dmk.doublejump.player.JumpPlayer;
import me.dmk.doublejump.player.JumpPlayerManager;
import me.dmk.doublejump.util.DurationUtil;
import me.dmk.doublejump.util.ItemUtil;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.World;
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
    private final MessageConfiguration messageConfiguration;
    private final NotificationSender notificationSender;
    private final JumpPlayerManager jumpPlayerManager;

    public JumpItemInteractListener(Server server, JumpConfiguration jumpConfiguration, MessageConfiguration messageConfiguration, NotificationSender notificationSender, JumpPlayerManager jumpPlayerManager) {
        this.server = server;
        this.jumpConfiguration = jumpConfiguration;
        this.messageConfiguration = messageConfiguration;
        this.notificationSender = notificationSender;
        this.jumpPlayerManager = jumpPlayerManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!this.jumpConfiguration.jumpItemEnabled) {
            return;
        }

        if (this.jumpConfiguration.jumpItemUsage != JumpItemUsage.CLICK_ITEM) {
            return;
        }

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null || !ItemUtil.compareItem(item, this.jumpConfiguration.jumpItem, true, !this.jumpConfiguration.jumpItemCancelEnchant)) {
            return;
        }

        GameMode playerGameMode = player.getGameMode();
        if (this.jumpConfiguration.disabledGameModes.contains(playerGameMode)) {
            event.setCancelled(true);

            this.notificationSender.sendMessage(player, this.messageConfiguration.jumpModeDisabledGameModeNotification);
            return;
        }

        World playerWorld = player.getWorld();
        if (this.jumpConfiguration.disabledWorlds.contains(playerWorld.getName())) {
            event.setCancelled(true);

            this.notificationSender.sendMessage(player, this.messageConfiguration.jumpModeDisabledWorldNotification);
            return;
        }

        if (this.jumpConfiguration.jumpItemUseDoubleJump) {
            JumpPlayer jumpPlayer = this.jumpPlayerManager.getJumpPlayer(player.getUniqueId())
                    .orElseGet(() -> this.jumpPlayerManager.add(player.getUniqueId(), new JumpPlayer()));

            if (!jumpPlayer.canUseJump()) {
                event.setCancelled(true);

                Notification notification = Notification.builder()
                        .fromNotification(this.messageConfiguration.jumpDelayNotification)
                        .placeholder("{TIME}", DurationUtil.toHumanReadable(jumpPlayer.getRemainingDelayDuration()))
                        .build();

                this.notificationSender.sendMessage(player, notification);
                return;
            }

            DoubleJumpEvent doubleJumpEvent = new DoubleJumpEvent(player, jumpPlayer);

            this.server.getPluginManager().callEvent(doubleJumpEvent);
        }

        if (this.jumpConfiguration.jumpItemUseSwitchDoubleJump) {
            if (this.jumpPlayerManager.isDoubleJumpMode(player)) {
                this.jumpPlayerManager.disable(player);
                this.notificationSender.sendMessage(player, this.messageConfiguration.jumpModeDisabledNotification);
            }
            else {
                this.jumpPlayerManager.enable(player, false);
                this.notificationSender.sendMessage(player, this.messageConfiguration.jumpModeEnabledNotification);
            }
        }

        int reduceJumpItemDurability = this.jumpConfiguration.reduceJumpItemDurability;
        if (reduceJumpItemDurability > 0) {
            if (item.getItemMeta() instanceof Damageable itemDamageable) {
                itemDamageable.setDamage(-(reduceJumpItemDurability - item.getType().getMaxDurability()));
                item.setItemMeta(itemDamageable);
            }
        }

        if (this.jumpConfiguration.removeJumpItemAfterUse) {
            player.getInventory().removeItem(item);
        }

        if (this.jumpConfiguration.disableJumpModeAfterUse) {
            this.jumpPlayerManager.remove(player.getUniqueId());
        }

        if (this.jumpConfiguration.cancelJumpItemUse) {
            event.setCancelled(true);
        }
    }
}
