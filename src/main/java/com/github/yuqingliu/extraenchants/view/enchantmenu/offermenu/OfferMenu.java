package com.github.yuqingliu.extraenchants.view.enchantmenu.offermenu;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.github.yuqingliu.extraenchants.api.Scheduler;
import com.github.yuqingliu.extraenchants.enchantment.EnchantmentOffer;
import com.github.yuqingliu.extraenchants.view.enchantmenu.EnchantMenu;
import com.github.yuqingliu.extraenchants.view.enchantmenu.EnchantMenu.MenuType;

import lombok.Getter;

@Getter
public class OfferMenu implements Listener {
    private final EnchantMenu enchantMenu;
    private final OfferMenuController controller;
    
    public OfferMenu(EnchantMenu enchantMenu) {
        this.enchantMenu = enchantMenu;
        this.controller = new OfferMenuController(enchantMenu);
        enchantMenu.getEventManager().registerEvent(this);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        Inventory actionInventory = event.getInventory();
        ItemStack currentItem = event.getCurrentItem();

        if (clickedInventory == null || currentItem == null || !event.getView().title().equals(enchantMenu.getDisplayName())) {
            return;
        }

        if(enchantMenu.getPlayerMenuTypes().get(player) != MenuType.OfferMenu) {
            return;
        }
        if(enchantMenu.isUnavailable(currentItem)) {
            event.setCancelled(true);
            return;
        }

        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            if(!Arrays.equals(enchantMenu.toCoords(event.getSlot()), controller.getItemSlot()) && !clickedInventory.equals(player.getInventory())) {
                event.setCancelled(true);
                return;
            }
            // Handle the shift-click case
            ItemStack itemInTargetSlot = actionInventory.getItem(enchantMenu.toIndex(controller.getItemSlot()));
            if (itemInTargetSlot == null || itemInTargetSlot.getType() == Material.AIR) {
                enchantMenu.setItem(actionInventory, controller.getItemSlot(), currentItem.clone());
                clickedInventory.clear(event.getSlot());
                event.setCancelled(true);
                controller.onClose(player);
                enchantMenu.getMainMenu().getController().openMenu(player, actionInventory);
            } else {
                controller.onClose(player);
                enchantMenu.getMainMenu().getController().openMenu(player, actionInventory);
            }
            return;
        }

        if (clickedInventory.equals(player.getOpenInventory().getTopInventory())) {
            int[] slot = enchantMenu.toCoords(event.getSlot());
            if(Arrays.equals(slot, controller.getItemSlot())) {
                controller.onClose(player);
                enchantMenu.getMainMenu().getController().openMenu(player, clickedInventory);
                return;
            }
            if(enchantMenu.rectangleContains(slot, controller.getEnchantOptions())) {
                event.setCancelled(true);
                // Apply offer
                int pageNumber = controller.getPageNumbers().get(player)[0];
                EnchantmentOffer offer = controller.getPageData().get(pageNumber).get(Arrays.asList(slot[0], slot[1]));
                controller.applyOffer(player, clickedInventory, offer);
                return;
            }  
            if(Arrays.equals(slot, controller.getNextPageButton())) {
                event.setCancelled(true);
                controller.nextPage(player, clickedInventory);
                return;
            } 
            if(Arrays.equals(slot, controller.getPrevPageButton())) {
                event.setCancelled(true);
                controller.prevPage(player, clickedInventory);
                return;
            } 
            if(Arrays.equals(slot, controller.getPrevMenuButton())) {
                event.setCancelled(true);
                controller.onClose(player);
                enchantMenu.getMainMenu().getController().openMenu(player, clickedInventory);
                return;
            } 
            if(Arrays.equals(slot, controller.getExitMenuButton())) {
                event.setCancelled(true);
                clickedInventory.close();
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory closedInventory = event.getInventory();
        Player player = (Player) event.getPlayer();
        if (event.getView().title().equals(enchantMenu.getDisplayName())) {
            ItemStack itemInEnchantSlot = closedInventory.getItem(enchantMenu.toIndex(controller.getItemSlot()));
            if (itemInEnchantSlot != null && itemInEnchantSlot.getType() != Material.AIR) {
                PlayerInventory playerInventory = player.getInventory();
                ItemStack currentItemInMainHand = playerInventory.getItemInMainHand();
                playerInventory.setItemInMainHand(itemInEnchantSlot);
                if (currentItemInMainHand != null && currentItemInMainHand.getType() != Material.AIR) {
                    HashMap<Integer, ItemStack> unaddedItems = playerInventory.addItem(currentItemInMainHand);
                    if (!unaddedItems.isEmpty()) {
                        player.getWorld().dropItemNaturally(player.getLocation(), currentItemInMainHand);
                    }
                }
                closedInventory.clear();
            }
            controller.onClose(player);
        }
    }    
}
