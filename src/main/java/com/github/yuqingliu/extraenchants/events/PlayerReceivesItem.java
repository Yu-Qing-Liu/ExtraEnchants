package com.github.yuqingliu.extraenchants.events;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.yuqingliu.extraenchants.ExtraEnchants;
import com.github.yuqingliu.extraenchants.item.items.Item;

public class PlayerReceivesItem implements Listener {
    private Map<String, Item> registry;

    public PlayerReceivesItem(ExtraEnchants plugin) {
        this.registry = plugin.getItemManager().getItemRegistry();
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        ItemStack result = event.getInventory().getResult();
        if (result != null && result.getType() != Material.AIR) {
            if(isNewItem(result)) {
                Item item = registry.get(result.getType().name());
                event.getInventory().setResult(item.getItem());
            }
        }
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        ItemStack result = event.getItem().getItemStack();
        if(isNewItem(result)) {
            Item item = registry.get(result.getType().name());
            event.getItem().setItemStack(item.getItem());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
            ItemStack result = event.getCurrentItem();
            if(isNewItem(result)) {
                Item item = registry.get(result.getType().name());
                event.setCurrentItem(item.getItem());
            }
        }
    }

    @EventHandler
    public void onInventoryCreative(InventoryCreativeEvent event) {
        ItemStack result = event.getCursor();
        if (result != null && result.getType() != Material.AIR) {
            if(isNewItem(result)) {
                Item item = registry.get(result.getType().name());
                event.setCursor(item.getItem());
            }
        }
    }

    private boolean isNewItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return true;
        }
        return false;
    }
}

