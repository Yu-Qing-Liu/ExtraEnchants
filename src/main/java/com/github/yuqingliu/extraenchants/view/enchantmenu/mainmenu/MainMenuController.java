package com.github.yuqingliu.extraenchants.view.enchantmenu.mainmenu;

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
import com.github.yuqingliu.extraenchants.view.enchantmenu.EnchantMenu;
import com.github.yuqingliu.extraenchants.view.enchantmenu.EnchantMenu.MenuType;

import lombok.Getter;

@Getter
public class MainMenuController {
    private final EnchantMenu enchantMenu;
    private final int[] nextPageButton = new int[]{6,0};
    private final int[] prevPageButton = new int[]{6,5};
    private final int[] itemSlot = new int[]{7,2};
    private final int[] decorationSlot = new int[]{7,3};
    private final int[] enchantOptionsStart = new int[]{0,0};
    private final int enchantOptionsLength = 6;
    private final int enchantOptionsWidth = 6;
    private final int enchantOptionsSize = enchantOptionsLength * enchantOptionsWidth;
    private final List<int[]> enchantOptions;
    private Map<Player, int[]> pageNumbers = new ConcurrentHashMap<>();
    private Map<Integer, Map<List<Integer>, Enchantment>> pageData = new ConcurrentHashMap<>();

    public MainMenuController(EnchantMenu enchantMenu) {
        this.enchantMenu = enchantMenu;
        this.enchantOptions = enchantMenu.rectangleArea(enchantOptionsStart, enchantOptionsWidth, enchantOptionsLength);
    }

    public void openMenu(Player player, Inventory inv) {
        pageNumbers.put(player, new int[]{1});
        Scheduler.runLaterAsync((task) -> {
            enchantMenu.getPlayerMenuTypes().put(player, MenuType.MainMenu);
        }, Duration.ofMillis(50));
        enchantMenu.fill(inv, enchantMenu.getBackgroundItems().get(Material.PURPLE_STAINED_GLASS_PANE));
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

    private void fetchOptions(Player player, Inventory inv) {
        ItemStack item = inv.getItem(enchantMenu.toIndex(itemSlot));
        Enchantment[] applicable = enchantMenu.getEnchantmentRepository().getApplicableEnchantments(item);
        Queue<Enchantment> offers = new ArrayDeque<>();
        for (Enchantment enchantment : applicable) {
            offers.offer(enchantment);
        }
        int maxPages = (int) Math.ceil((double) applicable.length / (double) enchantOptionsSize);
        for (int i = 0; i < maxPages; i++) {
            int pageNum = i + 1;
            Map<List<Integer>, Enchantment> options = new LinkedHashMap<>();
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
        Map<List<Integer>, Enchantment> options = pageData.getOrDefault(pageNumbers.get(player)[0], Collections.emptyMap());       
        for(Map.Entry<List<Integer>, Enchantment> entry : options.entrySet()) {
            List<Integer> coords = entry.getKey();
            Enchantment option = entry.getValue();
            if(option == null) {
                enchantMenu.setItem(inv, coords, enchantMenu.getUnavailable());
            } else {
                enchantMenu.setItem(inv, coords, enchantMenu.createSlotItem(Material.ENCHANTED_BOOK, option.getName()));;
            }
        }
    }

    private void buttons(Inventory inv) {
        enchantMenu.setItem(inv, nextPageButton, enchantMenu.getNextPage());
        enchantMenu.setItem(inv, prevPageButton, enchantMenu.getPrevPage());
        enchantMenu.setItem(inv, itemSlot, new ItemStack(Material.AIR));
        enchantMenu.setItem(inv, decorationSlot, new ItemStack(Material.ENCHANTING_TABLE));
    }

    public void onClose(Player player) {
        pageNumbers.remove(player);
    }
}
