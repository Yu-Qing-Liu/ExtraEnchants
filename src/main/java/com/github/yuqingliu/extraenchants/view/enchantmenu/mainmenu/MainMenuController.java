package com.github.yuqingliu.extraenchants.view.enchantmenu.mainmenu;

import java.time.Duration;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.github.yuqingliu.extraenchants.api.Scheduler;
import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
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
    private final int[] vanillaEnchantsButton = new int[]{0,0};
    private final int[] customEnchantsButton = new int[]{1,0};
    private final int[] abilityEnchantsButton = new int[]{2,0};

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
        if(enchantMenu.getEnchantmentRepository().getVanillaApplicableEnchantments(enchantMenu.getItem(inv, itemSlot)).length > 0) {
            enchantMenu.setItem(inv, vanillaEnchantsButton, enchantMenu.createSlotItem(Material.WRITTEN_BOOK, Component.text("Vanilla Enchants", NamedTextColor.GREEN)));
        }
        if(enchantMenu.getEnchantmentRepository().getCustomApplicableEnchantments(enchantMenu.getItem(inv, itemSlot)).length > 0) {
            enchantMenu.setItem(inv, customEnchantsButton, enchantMenu.createSlotItem(Material.WRITTEN_BOOK, Component.text("Custom Enchants", NamedTextColor.BLUE)));
        }
        if(enchantMenu.getEnchantmentRepository().getAbilityApplicableEnchantments(enchantMenu.getItem(inv, itemSlot)).length > 0) {
            enchantMenu.setItem(inv, abilityEnchantsButton, enchantMenu.createSlotItem(Material.WRITTEN_BOOK, Component.text("Ability Enchants", NamedTextColor.LIGHT_PURPLE)));
        }
    }

    public void clear(Inventory inv) {
        enchantMenu.fillRectangleArea(inv, new int[]{0,0}, 6, 6, enchantMenu.getUnavailable());
    }

    public void openVanillaSelectionMenu(Player player, Inventory inv) {
        Enchantment[] applicable = enchantMenu.getEnchantmentRepository().getVanillaApplicableEnchantments(enchantMenu.getItem(inv, itemSlot));
        enchantMenu.getSelectionMenu().getController().openMenu(player, inv, applicable);
    }

    public void openCustomSelectionMenu(Player player, Inventory inv) {
        Enchantment[] applicable = enchantMenu.getEnchantmentRepository().getCustomApplicableEnchantments(enchantMenu.getItem(inv, itemSlot));
        enchantMenu.getSelectionMenu().getController().openMenu(player, inv, applicable);
    }

    public void openAbilitySelectionMenu(Player player, Inventory inv) {
        Enchantment[] applicable = enchantMenu.getEnchantmentRepository().getAbilityApplicableEnchantments(enchantMenu.getItem(inv, itemSlot));
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
