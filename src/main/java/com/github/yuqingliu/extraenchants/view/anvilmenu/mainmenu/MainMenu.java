package com.github.yuqingliu.extraenchants.view.anvilmenu.mainmenu;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

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

import com.github.yuqingliu.extraenchants.api.Scheduler;
import com.github.yuqingliu.extraenchants.view.anvilmenu.AnvilMenu;
import com.github.yuqingliu.extraenchants.view.anvilmenu.AnvilMenu.MenuType;

import lombok.Getter;

@Getter
public class MainMenu implements Listener {
    private final AnvilMenu anvilMenu;
    private final MainMenuController controller;
    private boolean hasResult= false;
    
    public MainMenu(AnvilMenu anvilMenu) {
        this.anvilMenu = anvilMenu;
        this.controller = new MainMenuController(anvilMenu);
        anvilMenu.getManagerRepository().getEventManager().registerEvent(this);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getView().title().equals(anvilMenu.getDisplayName())) {
            Player player = (Player) event.getPlayer();
            wait(event.getInventory(), player);
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        Inventory actionInventory = event.getInventory();
        ItemStack currentItem = event.getCurrentItem();

        if (clickedInventory == null || currentItem == null || !event.getView().title().equals(anvilMenu.getDisplayName())) {
            return;
        }

        if(anvilMenu.getPlayerMenuTypes().get(player) != MenuType.MainMenu) {
            return;
        }

        if(anvilMenu.isUnavailable(currentItem) || currentItem.getType() == Material.ANVIL) {
            event.setCancelled(true);
            return;
        }

        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            // Handle the shift-click case
            ItemStack item1 = anvilMenu.getItem(actionInventory, controller.getSlot1());
            ItemStack item2 = anvilMenu.getItem(actionInventory, controller.getSlot2());

            if(!hasResult) {
                if (anvilMenu.isItemNull(item1))  {
                    event.setCancelled(true);
                    anvilMenu.setItem(actionInventory, controller.getSlot1(), currentItem.clone());
                    clickedInventory.clear(event.getSlot());
                } else if (!anvilMenu.isItemNull(item1) && anvilMenu.isItemNull(item2)) {
                    event.setCancelled(true);
                    anvilMenu.setItem(actionInventory, controller.getSlot2(), currentItem.clone());
                    clickedInventory.clear(event.getSlot());
                } 
            } else if(event.getSlot() == anvilMenu.toIndex(controller.getResultSlot())) {
                if (player.getLevel() >= controller.getCost()) {
                    controller.merge(player, actionInventory);
                    wait(actionInventory, player);
                } else {
                    event.setCancelled(true);
                    anvilMenu.getLogger().sendPlayerErrorMessage(player, "Not enough experience.");
                } 
            } else {
                controller.setBarrier(actionInventory);
                wait(actionInventory, player);
            }
            return;
        }

        if (clickedInventory.equals(player.getOpenInventory().getTopInventory())) {
            int[] slot = anvilMenu.toCoords(event.getSlot());
            if(Arrays.equals(slot, controller.getResultSlot())) {
                if (player.getLevel() >= controller.getCost()) {
                    controller.merge(player, clickedInventory);
                    wait(clickedInventory, player);
                } else {
                    event.setCancelled(true);
                    anvilMenu.getLogger().sendPlayerErrorMessage(player, "Not enough experience.");
                } 
                return;
            }
            if(Arrays.equals(slot, controller.getSlot1()) || Arrays.equals(slot, controller.getSlot2())) {
                controller.setBarrier(clickedInventory);
                wait(clickedInventory, player);
                return;
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory closedInventory = event.getInventory();
        Player player = (Player) event.getPlayer();
        if (event.getView().title().equals(anvilMenu.getDisplayName())) {
            ItemStack item1 = anvilMenu.getItem(closedInventory, controller.getSlot1());
            ItemStack item2 = anvilMenu.getItem(closedInventory, controller.getSlot2());
            if (!anvilMenu.isItemNull(item1)) {
                Inventory playerInventory = player.getInventory();
                Map<Integer, ItemStack> unaddedItems = playerInventory.addItem(item1);
                if (!unaddedItems.isEmpty()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), item1);
                }
            }
            if (!anvilMenu.isItemNull(item2)) {
                Inventory playerInventory = player.getInventory();
                Map<Integer, ItemStack> unaddedItems = playerInventory.addItem(item2);
                if (!unaddedItems.isEmpty()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), item2);
                }
            }
            closedInventory.clear();
        }   
    }

    private void wait(Inventory inv, Player player) {
        Scheduler.runTimerAsync(task -> {
            controller.setBarrier(inv);
            ItemStack item1 = anvilMenu.getItem(inv, controller.getSlot1());
            ItemStack item2 = anvilMenu.getItem(inv, controller.getSlot2());
            if(!anvilMenu.isItemNull(item1) && !anvilMenu.isItemNull(item2)) {
                controller.updateResult(inv, player, false, item1, item2);
                hasResult = true;
                task.cancel();
            }
        }, Duration.ofMillis(250), Duration.ZERO);
    }
}
