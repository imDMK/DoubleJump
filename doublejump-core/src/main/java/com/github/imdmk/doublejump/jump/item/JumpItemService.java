package com.github.imdmk.doublejump.jump.item;

import com.github.imdmk.doublejump.jump.item.configuration.JumpItemConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class JumpItemService {

    private final ItemStack jumpItem;
    private final JumpItemUsage jumpItemUsage;

    private final boolean ignoreEnchants;
    private final boolean ignoreDurability;

    public JumpItemService(JumpItemConfiguration jumpItemConfiguration) {
        this.jumpItem = jumpItemConfiguration.item;
        this.jumpItemUsage = jumpItemConfiguration.usageConfiguration.usage;
        this.ignoreEnchants = !jumpItemConfiguration.cancelEnchant;
        this.ignoreDurability = !jumpItemConfiguration.cancelRepair;
    }

    public boolean compare(ItemStack toCompare) {
        ItemMeta jumpItemMeta = this.jumpItem.getItemMeta();

        if (jumpItemMeta == null) {
            return this.jumpItem.equals(toCompare);
        }

        ItemStack toCompareClone = new ItemStack(toCompare);
        ItemMeta toCompareCloneMeta = toCompareClone.getItemMeta();

        if (toCompareCloneMeta == null) {
            return this.jumpItem.equals(toCompareClone);
        }

        if (this.ignoreDurability) {
            if (jumpItemMeta instanceof Damageable jumpDamageable && toCompareCloneMeta instanceof Damageable toCompareDamageable) {
                toCompareDamageable.setDamage(jumpDamageable.getDamage());
                toCompareClone.setItemMeta(toCompareDamageable);
            }
        }

        if (this.ignoreEnchants) {
            toCompareClone.getEnchantments().putAll(jumpItemMeta.getEnchants());
        }

        return toCompareClone.equals(this.jumpItem);
    }

    public boolean isCorrectlyUsed(Player player) {
        PlayerInventory playerInventory = player.getInventory();

        return switch (this.jumpItemUsage) {
            case WEAR_ITEM -> this.isWearing(player);
            case HOLD_ITEM -> {
                ItemStack itemInMainHand = playerInventory.getItemInMainHand();
                ItemStack itemInOffHand = playerInventory.getItemInOffHand();

                yield this.compare(itemInMainHand) || this.compare(itemInOffHand);
            }
            case HAVE_ITEM -> playerInventory.contains(this.jumpItem);
            default -> false;
        };
    }

    public boolean isWearing(Player player) {
        PlayerInventory playerInventory = player.getInventory();

        return switch (this.jumpItem.getType()) {
            case DIAMOND_HELMET, CHAINMAIL_HELMET, GOLDEN_HELMET, LEATHER_HELMET, IRON_HELMET, TURTLE_HELMET, NETHERITE_HELMET -> {
                ItemStack playerHelmet = playerInventory.getHelmet();

                if (playerHelmet == null) {
                    yield false;
                }

                yield this.compare(playerHelmet);
            }

            case DIAMOND_CHESTPLATE, CHAINMAIL_CHESTPLATE, GOLDEN_CHESTPLATE, IRON_CHESTPLATE, LEATHER_CHESTPLATE, NETHERITE_CHESTPLATE -> {
                ItemStack playerChestPlate = playerInventory.getChestplate();

                if (playerChestPlate == null) {
                    yield false;
                }

                yield this.compare(playerChestPlate);
            }

            case DIAMOND_LEGGINGS, CHAINMAIL_LEGGINGS, GOLDEN_LEGGINGS, IRON_LEGGINGS, LEATHER_LEGGINGS, NETHERITE_LEGGINGS -> {
                ItemStack playerLeggings = playerInventory.getLeggings();

                if (playerLeggings == null) {
                    yield false;
                }

                yield this.compare(playerLeggings);
            }

            case DIAMOND_BOOTS, CHAINMAIL_BOOTS, GOLDEN_BOOTS, IRON_BOOTS, LEATHER_BOOTS, NETHERITE_BOOTS -> {
                ItemStack playerBoots = playerInventory.getBoots();

                if (playerBoots == null) {
                    yield false;
                }

                yield this.compare(playerBoots);
            }

            default -> false;
        };
    }
}
