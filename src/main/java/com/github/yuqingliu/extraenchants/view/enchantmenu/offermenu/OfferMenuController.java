package com.github.yuqingliu.extraenchants.view.enchantmenu.offermenu;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.Scheduler;
import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.enchantment.EnchantmentOffer;
import com.github.yuqingliu.extraenchants.view.enchantmenu.EnchantMenu;
import com.github.yuqingliu.extraenchants.view.enchantmenu.EnchantMenu.MenuType;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@Getter
public class OfferMenuController {
    private final EnchantMenu enchantMenu;
    private final int[] nextPageButton = new int[]{6,0};
    private final int[] prevPageButton = new int[]{6,5};
    private final int[] prevMenuButton = new int[]{8,0};
    private final int[] exitMenuButton = new int[]{8,5};
    private final int[] itemSlot = new int[]{7,2};
    private final int[] decorationSlot = new int[]{7,3};
    private final int[] enchantOptionsStart = new int[]{0,0};
    private final int enchantOptionsLength = 6;
    private final int enchantOptionsWidth = 6;
    private final int enchantOptionsSize = enchantOptionsLength * enchantOptionsWidth;
    private final List<int[]> enchantOptions;
    private Map<Player, int[]> pageNumbers = new ConcurrentHashMap<>();
    private Map<Integer, Map<List<Integer>, EnchantmentOffer>> pageData = new ConcurrentHashMap<>();
    private Map<Player, Enchantment> selectedEnchantments = new ConcurrentHashMap<>();

    public OfferMenuController(EnchantMenu enchantMenu) {
        this.enchantMenu = enchantMenu;
        this.enchantOptions = enchantMenu.rectangleArea(enchantOptionsStart, enchantOptionsWidth, enchantOptionsLength);
    }

    public void openMenu(Player player, Inventory inv, Enchantment enchantment) {
        pageNumbers.put(player, new int[]{1});
        selectedEnchantments.put(player, enchantment);
        Scheduler.runLaterAsync((task) -> {
            enchantMenu.getPlayerMenuTypes().put(player, MenuType.OfferMenu);
        }, Duration.ofMillis(50));
        buttons(inv);
        fetchOptions(player, inv);
        displayOptions(player, inv);
    }

    public void nextPage(Player player, Inventory inv) {
        pageNumbers.get(player)[0]++;
        if(pageData.containsKey(pageNumbers.get(player)[0])) {
            displayOptions(player, inv); 
        } else {
            pageNumbers.get(player)[0]--;
        }     
    }

    public void prevPage(Player player, Inventory inv) {
        pageNumbers.get(player)[0]--;
        if(pageNumbers.get(player)[0] > 0) {
            displayOptions(player, inv);
        } else {
            pageNumbers.get(player)[0]++;
        }
    }

    public void clear(Inventory inv) {
        enchantMenu.fillRectangleArea(inv, enchantOptionsStart, enchantOptionsWidth, enchantOptionsLength, enchantMenu.getUnavailable());
    }

    public void applyOffer(Player player, Inventory inv, EnchantmentOffer offer) {
        ItemStack item = enchantMenu.getItem(inv, itemSlot);
        if(item != null && item.getType() != Material.AIR) {
            ItemStack result = offer.applyOffer(player, item);
            enchantMenu.setItem(inv, itemSlot, result);
            enchantMenu.getMainMenu().getController().openMenu(player, inv);
        } else {
            enchantMenu.getLogger().sendPlayerErrorMessage(player, "Could not apply offer.");
        }
    }

    private void fetchOptions(Player player, Inventory inv) {
        pageData.clear();
        Enchantment selectedEnchantment = selectedEnchantments.get(player);
        Queue<EnchantmentOffer> offers = new ArrayDeque<>();
        for (int i = 1; i <= selectedEnchantment.getMaxLevel(); i++) {
            int requiredLevel = enchantMenu.getMathManager().evaluateExpression(selectedEnchantment.getRequiredLevelFormula(), i);
            int cost = enchantMenu.getMathManager().evaluateExpression(selectedEnchantment.getCostFormula(), i);
            EnchantmentOffer offer = new EnchantmentOffer(enchantMenu.getSoundManager(), selectedEnchantment, i, requiredLevel, cost);
            offers.offer(offer);
        }
        int maxPages = (int) Math.ceil((double) offers.size() / (double) enchantOptionsSize);
        for (int i = 0; i < maxPages; i++) {
            int pageNum = i + 1;
            Map<List<Integer>, EnchantmentOffer> options = new LinkedHashMap<>();
            for(int[] coords : this.enchantOptions) {
                if(offers.isEmpty()) {
                    options.put(Arrays.asList(coords[0], coords[1]), null);
                } else {
                    options.put(Arrays.asList(coords[0], coords[1]), offers.poll());
                }
            }
            pageData.put(pageNum, options);
        }
    }

    private void displayOptions(Player player, Inventory inv) {
        Map<List<Integer>, EnchantmentOffer> options = pageData.getOrDefault(pageNumbers.get(player)[0], Collections.emptyMap());       
        for(Map.Entry<List<Integer>, EnchantmentOffer> entry : options.entrySet()) {
            List<Integer> coords = entry.getKey();
            EnchantmentOffer offer = entry.getValue();
            if(offer == null) {
                enchantMenu.setItem(inv, coords, enchantMenu.getUnavailable());
            } else {
                Component displayName = offer.getEnchantment().getLeveledName(offer.getLevel());
                Component levelRequired = Component.text("LEVEL REQUIRED: ", NamedTextColor.GREEN).append(Component.text(offer.getRequiredLevel(), NamedTextColor.DARK_GREEN));
                Component enchantCost = Component.text("EXP COST: ", NamedTextColor.GREEN).append(Component.text(offer.getCost(), NamedTextColor.DARK_GREEN));
                ItemStack icon = enchantMenu.createSlotItem(Material.ENCHANTED_BOOK, displayName, Arrays.asList(levelRequired, enchantCost));
                enchantMenu.setItem(inv, coords, icon);
            }
        }
    }

    private void buttons(Inventory inv) {
        enchantMenu.setItem(inv, nextPageButton, enchantMenu.getNextPage());
        enchantMenu.setItem(inv, prevPageButton, enchantMenu.getPrevPage());
        enchantMenu.setItem(inv, prevMenuButton, enchantMenu.getPrevMenu());
        enchantMenu.setItem(inv, exitMenuButton, enchantMenu.getExitMenu());
        enchantMenu.setItem(inv, decorationSlot, enchantMenu.getEnchantingTable());
    }

    public void onClose(Player player) {
        pageNumbers.remove(player);
        selectedEnchantments.remove(player);
        pageData.clear();
    }
}
