package com.github.yuqingliu.extraenchants.view.grindstonemenu.mainmenu;

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
import org.bukkit.inventory.PlayerInventory;

import com.github.yuqingliu.extraenchants.api.Scheduler;
import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.view.grindstonemenu.GrindstoneMenu;
import com.github.yuqingliu.extraenchants.view.grindstonemenu.GrindstoneMenu.MenuType;

import lombok.Getter;

@Getter
public class MainMenu implements Listener {
    private final GrindstoneMenu grindstoneMenu;
    private final MainMenuController controller;
    private boolean hasResult= false;
    
    public MainMenu(GrindstoneMenu grindstoneMenu) {
        this.grindstoneMenu = grindstoneMenu;
        this.controller = new MainMenuController(grindstoneMenu);
        grindstoneMenu.getManagerRepository().getEventManager().registerEvent(this);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getView().title().equals(grindstoneMenu.getDisplayName())) {
            Player player = (Player) event.getPlayer();
            wait(player, event.getInventory());
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        Inventory actionInventory = event.getInventory();
        ItemStack currentItem = event.getCurrentItem();

        if (clickedInventory == null || currentItem == null || !event.getView().title().equals(grindstoneMenu.getDisplayName())) {
            return;
        }

        if(grindstoneMenu.getPlayerMenuTypes().get(player) != MenuType.MainMenu) {
            return;
        }

        if(grindstoneMenu.isUnavailable(currentItem) || currentItem.getType() == Material.ANVIL) {
            event.setCancelled(true);
            return;
        }

        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            if(!Arrays.equals(grindstoneMenu.toCoords(event.getSlot()), controller.getItemSlot()) && !clickedInventory.equals(player.getInventory())) {
                event.setCancelled(true);
                return;
            }
            // Handle the shift-click case
            ItemStack itemInTargetSlot = actionInventory.getItem(grindstoneMenu.toIndex(controller.getItemSlot()));
            if (itemInTargetSlot == null || itemInTargetSlot.getType() == Material.AIR) {
                event.setCancelled(true);
                grindstoneMenu.setItem(actionInventory, controller.getItemSlot(), currentItem.clone());
                clickedInventory.clear(event.getSlot());
                controller.reload(player, actionInventory);
            } else {
                controller.clear(actionInventory);
                wait(player, actionInventory);
            }
            return;
        }

        if (clickedInventory.equals(player.getOpenInventory().getTopInventory())) {
            int[] slot = grindstoneMenu.toCoords(event.getSlot());
            if(Arrays.equals(slot, controller.getItemSlot())) {
                controller.clear(clickedInventory);
                wait(player, clickedInventory);
                return;
            }
            if(grindstoneMenu.rectangleContains(slot, controller.getEnchantOptions())) {
                event.setCancelled(true);
                int pageNumber = controller.getPageNumbers().get(player)[0];
                Enchantment selected = controller.getPageData().get(pageNumber).get(Arrays.asList(slot[0], slot[1]));
                controller.removeEnchantment(player, clickedInventory, selected);
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
        if (event.getView().title().equals(grindstoneMenu.getDisplayName())) {
            ItemStack itemInEnchantSlot = closedInventory.getItem(grindstoneMenu.toIndex(controller.getItemSlot()));
            if (itemInEnchantSlot != null && itemInEnchantSlot.getType() != Material.AIR) {
                PlayerInventory playerInventory = player.getInventory();
                ItemStack currentItemInMainHand = playerInventory.getItemInMainHand();
                playerInventory.setItemInMainHand(itemInEnchantSlot);
                if (currentItemInMainHand != null && currentItemInMainHand.getType() != Material.AIR) {
                    Map<Integer, ItemStack> unaddedItems = playerInventory.addItem(currentItemInMainHand);
                    if (!unaddedItems.isEmpty()) {
                        player.getWorld().dropItemNaturally(player.getLocation(), currentItemInMainHand);
                    }
                }
                closedInventory.clear();
            }
        }
    }

    private void wait(Player player, Inventory inv) {
        Scheduler.runTimerAsync(task -> {
            ItemStack itemInTargetSlot = grindstoneMenu.getItem(inv, controller.getItemSlot());
            if (itemInTargetSlot != null && itemInTargetSlot.getType() != Material.AIR) {
                controller.reload(player, inv);
                task.cancel();
            }
        }, Duration.ofMillis(250), Duration.ZERO);
    }
}
