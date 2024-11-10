package com.github.yuqingliu.extraenchants.view.enchantmenu.mainmenu;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.Scheduler;
import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.Rarity;
import com.github.yuqingliu.extraenchants.view.enchantmenu.EnchantMenu;
import com.github.yuqingliu.extraenchants.view.enchantmenu.EnchantMenu.MenuType;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@Getter
public class MainMenuController {
    private final EnchantMenu enchantMenu;
    private final int[] exitMenuButton = new int[]{8,0};
    private final int[] itemSlot = new int[]{7,2};
    private final int[] decorationSlot = new int[]{7,3};
    private final int[] common = new int[]{1,2};
    private final int[] uncommon = new int[]{2,2};
    private final int[] rare = new int[]{3,2};
    private final int[] epic = new int[]{4,2};
    private final int[] legendary = new int[]{2,3};
    private final int[] mythic = new int[]{3,3};
    private final List<int[]> options = Arrays.asList(common, uncommon, rare, epic, legendary, mythic);

    public MainMenuController(EnchantMenu enchantMenu) {
        this.enchantMenu = enchantMenu;
    }

    public void openMenu(Player player, Inventory inv) {
        Scheduler.runLaterAsync((task) -> {
            enchantMenu.getPlayerMenuTypes().put(player, MenuType.MainMenu);
        }, Duration.ofMillis(50));
        clear(inv);
        reload(inv);
        border(inv);
        buttons(inv);
    }

    public void reload(Inventory inv) {
        ItemStack item = enchantMenu.getItem(inv, itemSlot);
        if(enchantMenu.isItemNull(item)) {
            return;
        }
        int i = 0;
        for(Rarity rarity : Rarity.values()) {
            int numElements = enchantMenu.getEnchantmentRepository().getApplicableEnchantmentsByRarity(item, rarity).length;
            Component title = Component.text(rarity.name() + " ENCHANTMENTS", rarity.color());
            Component data = Component.text("Applicable: ", NamedTextColor.GOLD).append(Component.text(numElements, NamedTextColor.YELLOW));
            enchantMenu.setItem(inv, options.get(i), enchantMenu.createSlotItem(Material.WRITTEN_BOOK, title, data));
            i++;
        }
    }

    public void clear(Inventory inv) {
        enchantMenu.fillRectangleArea(inv, new int[]{0,0}, 6, 6, enchantMenu.getUnavailable());
    }

    public void openRarityMenu(Player player, Inventory inv, Rarity rarity) {
        Enchantment[] applicable = enchantMenu.getEnchantmentRepository().getApplicableEnchantmentsByRarity(enchantMenu.getItem(inv, itemSlot), rarity);
        enchantMenu.getSelectionMenu().getController().openMenu(player, inv, applicable);
    }

    private void buttons(Inventory inv) {
        enchantMenu.setItem(inv, exitMenuButton, enchantMenu.getExitMenu());
        enchantMenu.setItem(inv, decorationSlot, enchantMenu.getEnchantingTable());
    }

    private void border(Inventory inv) {
        enchantMenu.fillRectangleArea(inv, new int[]{6,0}, 6, 1, enchantMenu.getBackgroundItems().get(Material.PURPLE_STAINED_GLASS_PANE));
        enchantMenu.fillRectangleArea(inv, new int[]{8,0}, 6, 1, enchantMenu.getBackgroundItems().get(Material.PURPLE_STAINED_GLASS_PANE));
        enchantMenu.fillRectangleArea(inv, new int[]{7,0}, 2, 1, enchantMenu.getBackgroundItems().get(Material.PURPLE_STAINED_GLASS_PANE));
        enchantMenu.fillRectangleArea(inv, new int[]{7,4}, 2, 1, enchantMenu.getBackgroundItems().get(Material.PURPLE_STAINED_GLASS_PANE));
    }
}
