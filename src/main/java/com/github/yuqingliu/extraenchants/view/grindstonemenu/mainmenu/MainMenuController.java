package com.github.yuqingliu.extraenchants.view.grindstonemenu.mainmenu;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.Scheduler;
import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.view.grindstonemenu.GrindstoneMenu;
import com.github.yuqingliu.extraenchants.view.grindstonemenu.GrindstoneMenu.MenuType;

import lombok.Getter;

@Getter
public class MainMenuController {
    private final GrindstoneMenu grindstoneMenu;
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
    private Map<Integer, Map<List<Integer>, Enchantment>> pageData = new ConcurrentHashMap<>();

    public MainMenuController(GrindstoneMenu grindstoneMenu) {
        this.grindstoneMenu = grindstoneMenu;
        this.enchantOptions = grindstoneMenu.rectangleArea(enchantOptionsStart, enchantOptionsWidth, enchantOptionsLength);
    }

    public void openMenu(Player player, Inventory inv) {
        pageNumbers.put(player, new int[]{1});
        Scheduler.runLaterAsync((task) -> {
            grindstoneMenu.getPlayerMenuTypes().put(player, MenuType.MainMenu);
        }, Duration.ofMillis(50));
        border(inv);
        buttons(inv);
        reload(player, inv);;
    }

    public void reload(Player player, Inventory inv) {
        clear(inv);
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
        grindstoneMenu.fillRectangleArea(inv, enchantOptionsStart, enchantOptionsWidth, enchantOptionsLength, grindstoneMenu.getUnavailable());
    }

    public void removeEnchantment(Player player, Inventory inv, Enchantment enchant) {
        ItemStack item = grindstoneMenu.getItem(inv, itemSlot);
        enchant.removeEnchantment(item);
        grindstoneMenu.getManagerRepository().getSoundManager().playGrindstoneSound(player);
        ExperienceOrb orb = player.getWorld().spawn(grindstoneMenu.getLocation(player), ExperienceOrb.class);
        orb.setExperience(3);
        reload(player, inv);
    }

    private void fetchOptions(Player player, Inventory inv) {
        pageData.clear();
        ItemStack item = grindstoneMenu.getItem(inv, itemSlot);
        Set<Enchantment> applicable = grindstoneMenu.getEnchantmentRepository().getEnchantments(item).keySet();
        Queue<Enchantment> offers = new ArrayDeque<>();
        for (Enchantment enchantment : applicable) {
            offers.offer(enchantment);
        }
        int maxPages = (int) Math.ceil((double) applicable.size() / (double) enchantOptionsSize);
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
                grindstoneMenu.setItem(inv, coords, grindstoneMenu.getUnavailable());
            } else {
                grindstoneMenu.setItem(inv, coords, grindstoneMenu.createSlotItem(Material.ENCHANTED_BOOK, option.getName()));
            }
        }
    }

    private void buttons(Inventory inv) {
        grindstoneMenu.setItem(inv, nextPageButton, grindstoneMenu.getNextPage());
        grindstoneMenu.setItem(inv, prevPageButton, grindstoneMenu.getPrevPage());
        grindstoneMenu.setItem(inv, exitMenuButton, grindstoneMenu.getExitMenu());
        grindstoneMenu.setItem(inv, decorationSlot, grindstoneMenu.getGrindStone());
    }

    private void border(Inventory inv) {
        ItemStack border = grindstoneMenu.getBackgroundItems().get(Material.ORANGE_STAINED_GLASS_PANE);
        grindstoneMenu.fillRectangleArea(inv, new int[]{6,1}, 4, 1, border);
        grindstoneMenu.fillRectangleArea(inv, new int[]{8,0}, 6, 1, border);
        grindstoneMenu.fillRectangleArea(inv, new int[]{7,0}, 2, 1, border);
        grindstoneMenu.fillRectangleArea(inv, new int[]{7,4}, 2, 1, border);
    }

    public void onClose(Player player) {
        pageNumbers.remove(player);
        pageData.clear();
    }
}
