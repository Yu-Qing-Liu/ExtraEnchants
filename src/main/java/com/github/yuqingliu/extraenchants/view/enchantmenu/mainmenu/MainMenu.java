package com.github.yuqingliu.extraenchants.view.enchantmenu.mainmenu;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.view.enchantmenu.EnchantMenu;
import com.github.yuqingliu.extraenchants.view.enchantmenu.EnchantMenu.MenuType;

public class MainMenu implements Listener {
    private final EnchantMenu enchantMenu;
    private final MainMenuController controller;
    
    public MainMenu(EnchantMenu enchantMenu) {
        this.enchantMenu = enchantMenu;
        this.controller = new MainMenuController(enchantMenu);
        enchantMenu.getEventManager().registerEvent(this);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        ItemStack currentItem = event.getCurrentItem();

        if (clickedInventory == null || currentItem == null || !event.getView().title().equals(enchantMenu.getDisplayName())) {
            return;
        }

        event.setCancelled(true);

        if(enchantMenu.getPlayerMenuTypes().get(player) == MenuType.MainMenu && clickedInventory.equals(player.getOpenInventory().getTopInventory())) {
            int[] slot = enchantMenu.toCoords(event.getSlot());
            if(enchantMenu.isUnavailable(currentItem)) {
                return;
            }
        }
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().title().equals(enchantMenu.getDisplayName())) {
            controller.onClose((Player) event.getPlayer());
        }
    }
}
