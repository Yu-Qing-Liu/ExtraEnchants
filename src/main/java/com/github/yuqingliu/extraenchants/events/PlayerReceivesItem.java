package com.github.yuqingliu.extraenchants.events;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.yuqingliu.extraenchants.ExtraEnchantsImpl;
import com.github.yuqingliu.extraenchants.item.items.Item;
import com.github.yuqingliu.extraenchants.utils.TextUtils;

public class PlayerReceivesItem implements Listener {
    private Map<String, Item> registry;

    public PlayerReceivesItem(ExtraEnchantsImpl plugin) {
        this.registry = plugin.getItemManager().getItemRegistry();
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        ItemStack result = event.getInventory().getResult();
        if (result != null && result.getType() != Material.AIR) {
            if(isNewItem(result)) {
                Item item = registry.get(result.getType().name());
                event.getInventory().setResult(item.getItem(result.getAmount()));
            }
        }
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        ItemStack result = event.getItem().getItemStack();
        if(isNewItem(result)) {
            Item item = registry.get(result.getType().name());
            event.getItem().setItemStack(item.getItem(1));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
            ItemStack result = event.getCurrentItem();
            if(isNewItem(result)) {
                Item item = registry.get(result.getType().name());
                event.setCurrentItem(item.getItem(1));
            }
        }
    }

    @EventHandler
    public void onCreativeInventoryClick(InventoryCreativeEvent event) {
        if (event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
            ItemStack result = event.getCursor();
            if(isNewItem(result)) {
                Item item = registry.get(result.getType().name());
                event.setCursor(item.getItem(1));
            }
        }
    }

    private boolean isNewItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        if(TextUtils.componentToString(meta.displayName()).contains("Page")) {
            return false;
        }
        if(TextUtils.componentToString(meta.displayName()).contains("Unavailable")) {
            return false;
        }
        if(TextUtils.componentToString(meta.displayName()).contains("Slot")) {
            return false;
        }
        if(item.getType() == Material.ENCHANTED_BOOK) {
            return false;
        }
        if (meta == null || !meta.hasLore()) {
            return true;
        }

        return false;
    }
}

