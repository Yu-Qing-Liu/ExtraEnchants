package com.github.yuqingliu.extraenchants.view.anvilmenu.mainmenu;

import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.yuqingliu.extraenchants.api.Scheduler;
import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.item.ItemImpl;
import com.github.yuqingliu.extraenchants.view.anvilmenu.AnvilMenu;
import com.github.yuqingliu.extraenchants.view.anvilmenu.AnvilMenu.MenuType;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@Getter
public class MainMenuController {
    private final AnvilMenu anvilMenu;
    private final int[] slot1 = new int[]{2,1};
    private final int[] slot2 = new int[]{4,1};
    private final int[] resultSlot = new int[]{6,1};
    private final int[] placeholder1 = new int[]{2,2};
    private final int[] placeholder2 = new int[]{4,2};
    private final int[] placeholder3 = new int[]{6,2};
    private final double repairCostPerResource = 1.5;
    private final double anvilCostPerLevel = 2;
    private int cost = 0;

    public MainMenuController(AnvilMenu anvilMenu) {
        this.anvilMenu = anvilMenu;
    }

    public void openMenu(Player player, Inventory inv) {
        Scheduler.runLaterAsync((task) -> {
            anvilMenu.getPlayerMenuTypes().put(player, MenuType.MainMenu);
        }, Duration.ofMillis(50));
        clear(inv);
        reload(inv);
        border(inv);
        placeholders(inv);
    }

    public void reload(Inventory inv) {

    }

    public void clear(Inventory inv) {

    }

    public void setBarrier(Inventory inv) {
        anvilMenu.setItem(inv, resultSlot, anvilMenu.getUnavailableItems().get(Material.BARRIER));
    }

    public void updateResult(Inventory inv, Player player, boolean recursion, ItemStack item1, ItemStack item2) {
        this.cost = 0;

        ItemStack result = item1.clone();
        if(canRepair(item1, item2)) {
            this.cost += repairItem(result, item1.getAmount());
            anvilMenu.setItem(inv, resultSlot, result);
        } else if(canRepair(item2, item1)) {
            this.cost += repairItem(result, item2.getAmount());
            anvilMenu.setItem(inv, resultSlot, result);
        }

        if(!isCompatible(item1, item2)) {
            return;
        }

        result = applyEnchants(result, anvilMenu.getEnchantmentRepository().getEnchantments(item1), anvilMenu.getEnchantmentRepository().getEnchantments(item2));

        if(this.cost == 0 && !recursion) {
            updateResult(inv, player, true, item2, item1);
        }

        repairIdenticalItem(result, item2);
        
        anvilMenu.setItem(inv, resultSlot, result);
        anvilMenu.setItem(inv, placeholder3, anvilMenu.createSlotItem(Material.ANVIL, Component.text("Cost: " + cost, NamedTextColor.GREEN)));
    }

    public void merge(Player player, Inventory inv) {
        player.setLevel(player.getLevel() - cost);
        anvilMenu.getManagerRepository().getSoundManager().playAnvilSound(player);
        anvilMenu.setItem(inv, slot1, new ItemStack(Material.AIR));
        anvilMenu.setItem(inv, slot2, new ItemStack(Material.AIR));
    }

    private ItemStack applyEnchants(ItemStack result, Map<Enchantment, Integer> item1Enchants, Map<Enchantment, Integer> item2Enchants) {
        int levels = 0;
        Set<Enchantment> keys = new HashSet<>(item1Enchants.keySet());
        keys.addAll(item2Enchants.keySet());
        ItemStack finalItem = result.clone();
        for(Enchantment enchant : keys) {
            int maxEnchantmentLevel = enchant.getMaxLevel();
            int itemEnchantLevel = 0;
            int sacrificeEnchantLevel = 0;
            if(item1Enchants.containsKey(enchant)) {
                itemEnchantLevel = item1Enchants.get(enchant);
            } 
            if(item2Enchants.containsKey(enchant)) {
                sacrificeEnchantLevel = item2Enchants.get(enchant);
            } 
            if(sacrificeEnchantLevel > itemEnchantLevel && enchant.canEnchant(finalItem)) {
                finalItem = enchant.removeEnchantment(finalItem);
                finalItem = enchant.applyEnchantment(finalItem, sacrificeEnchantLevel);
                levels += sacrificeEnchantLevel - itemEnchantLevel;
            } else if (sacrificeEnchantLevel == itemEnchantLevel && sacrificeEnchantLevel + 1 <= maxEnchantmentLevel && enchant.canEnchant(finalItem)) {
                finalItem = enchant.removeEnchantment(finalItem);
                finalItem = enchant.applyEnchantment(finalItem, sacrificeEnchantLevel + 1);
                levels += 1;
            }
        }
        this.cost += (int) (levels * anvilCostPerLevel);
        if(finalItem == null) {
            return result;
        }
        return finalItem;
    }

    private void repairIdenticalItem(ItemStack item1, ItemStack item2) {
        int maxDurability = item1.getType().getMaxDurability();
        if (item1.hasItemMeta() && item2.hasItemMeta()) {
            ItemMeta meta1 = item1.getItemMeta();
            ItemMeta meta2 = item2.getItemMeta();
            if (meta1 instanceof Damageable && meta2 instanceof Damageable) {
                Damageable damageable1 = (Damageable) meta1;
                Damageable damageable2 = (Damageable) meta2; // Corrected to meta2
                int damage1 = damageable1.getDamage();
                int damage2 = damageable2.getDamage();
                int totalDurability = (maxDurability - damage1) + (maxDurability - damage2);
                int repairBonus = (int) (maxDurability * 0.05);
                totalDurability += repairBonus;
                totalDurability = Math.min(totalDurability, maxDurability);
                int newDamage = maxDurability - totalDurability;
                damageable1.setDamage(newDamage);
                item1.setItemMeta(meta1);
            }
        }
    }

    private boolean isCompatible(ItemStack item1, ItemStack item2) {
        Item i1 = new ItemImpl(item1);
        Item i2 = new ItemImpl(item2);
        Map<Item, Set<Item>> anvilCombinations = anvilMenu.getAnvilRepository().getAnvilCombinations();
        if(i1.getMaterial() == Material.ENCHANTED_BOOK || i2.getMaterial() == Material.ENCHANTED_BOOK) {
            return true;
        } 
        if (i1.getMaterial() == i2.getMaterial()) {
            return true;
        }
        Set<Item> applicable1 = anvilCombinations.get(i1);
        Set<Item> applicable2 = anvilCombinations.get(i2);
        return applicable2.contains(i1) || applicable1.contains(i2);
    }

    private int repairItem(ItemStack item, int numResources) {
        int maxDurability = item.getType().getMaxDurability();
        int repairAmount = (int) ((double) maxDurability / 4.0) * numResources;
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof Damageable) {
                Damageable damageable = (Damageable) meta;
                int damage = damageable.getDamage();
                int newDamage = Math.max(0, damage - repairAmount);
                damageable.setDamage(newDamage);
                item.setItemMeta(meta);
            }
        }
        return (int) (numResources * repairCostPerResource);
    }

    private boolean canRepair(ItemStack repairable, ItemStack repaired) {
        if(repairable.getType() == Material.STRING && (repaired.getType() == Material.BOW || repaired.getType() == Material.CROSSBOW)) {
            return true;
        } 
        return repairable.canRepair(repaired);
    }

    private void border(Inventory inv) {
        ItemStack background = anvilMenu.getBackgroundItems().get(Material.BLACK_STAINED_GLASS_PANE);
        anvilMenu.fillRectangleArea(inv, new int[]{0,0}, 4, 2, background);
        anvilMenu.fillRectangleArea(inv, new int[]{3,0}, 4, 1, background);
        anvilMenu.fillRectangleArea(inv, new int[]{5,0}, 4, 1, background);
        anvilMenu.fillRectangleArea(inv, new int[]{7,0}, 4, 2, background);
        anvilMenu.fillRectangleArea(inv, new int[]{0,0}, 1, 9, background);
        anvilMenu.fillRectangleArea(inv, new int[]{0,3}, 1, 9, background);
    }

    private void placeholders(Inventory inv) {
        anvilMenu.setItem(inv, placeholder1, anvilMenu.getUnavailableItems().get(Material.IRON_BLOCK));
        anvilMenu.setItem(inv, placeholder2, anvilMenu.getUnavailableItems().get(Material.IRON_BLOCK));
        anvilMenu.setItem(inv, placeholder3, anvilMenu.getAnvil());
    }
}
