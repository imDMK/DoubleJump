package me.dmk.doublejump.util;

import me.dmk.doublejump.jump.item.JumpItemUsage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtil {

    private ItemUtil() {
        throw new UnsupportedOperationException("This is utility class.");
    }

    public static boolean compareItem(ItemStack item, ItemStack toCompare, boolean ignoreDurability, boolean ignoreEnchants) {
        if (item.getType() != toCompare.getType()) {
            return false;
        }

        ItemMeta itemMeta = item.getItemMeta();

        ItemStack toCompareClone = new ItemStack(toCompare.clone());
        ItemMeta toCompareMeta = toCompareClone.getItemMeta();

        if (toCompareMeta == null) {
            return item.equals(toCompare);
        }

        if (ignoreDurability) {
            if (itemMeta instanceof Damageable itemDamageable && toCompareMeta instanceof Damageable toCompareDamageable) {
                toCompareDamageable.setDamage(itemDamageable.getDamage());
                toCompareClone.setItemMeta(toCompareDamageable);
            }
        }

        if (ignoreEnchants) {
            item.getEnchantments().putAll(toCompareMeta.getEnchants());
            toCompareMeta.getEnchants().putAll(item.getEnchantments());
        }

        return item.equals(toCompareClone);
    }

    public static boolean isCorrectlyUsed(Player player, ItemStack item, JumpItemUsage itemUsage) {
        PlayerInventory playerInventory = player.getInventory();

        ItemStack itemInMainHand = playerInventory.getItemInMainHand();
        ItemStack itemInOffHand = playerInventory.getItemInOffHand();

        return switch (itemUsage) {
            case WEAR_ITEM -> ItemUtil.isWearingItem(player, item);
            case HOLD_ITEM -> itemInMainHand.equals(item) || itemInOffHand.equals(item);
            case HAVE_ITEM -> playerInventory.contains(item);
            default -> false;
        };
    }

    public static boolean isWearingItem(Player player, ItemStack itemStack) {
        PlayerInventory playerInventory = player.getInventory();

        ItemStack playerHelmet = playerInventory.getHelmet();
        ItemStack playerChestPlate = playerInventory.getChestplate();
        ItemStack playerLeggings = playerInventory.getLeggings();
        ItemStack playerBoots = playerInventory.getBoots();

        return switch (itemStack.getType()) {
            case DIAMOND_HELMET, CHAINMAIL_HELMET, GOLDEN_HELMET, LEATHER_HELMET, IRON_HELMET, TURTLE_HELMET,
                    NETHERITE_HELMET -> playerHelmet != null && playerHelmet.equals(itemStack);

            case DIAMOND_CHESTPLATE, CHAINMAIL_CHESTPLATE, GOLDEN_CHESTPLATE, IRON_CHESTPLATE, LEATHER_CHESTPLATE,
                    NETHERITE_CHESTPLATE -> playerChestPlate != null && playerChestPlate.equals(itemStack);

            case DIAMOND_LEGGINGS, CHAINMAIL_LEGGINGS, GOLDEN_LEGGINGS, IRON_LEGGINGS, LEATHER_LEGGINGS,
                    NETHERITE_LEGGINGS -> playerLeggings != null && playerLeggings.equals(itemStack);

            case DIAMOND_BOOTS, CHAINMAIL_BOOTS, GOLDEN_BOOTS, IRON_BOOTS, LEATHER_BOOTS,
                    NETHERITE_BOOTS -> playerBoots != null && playerBoots.equals(itemStack);

            default -> false;
        };
    }
}
