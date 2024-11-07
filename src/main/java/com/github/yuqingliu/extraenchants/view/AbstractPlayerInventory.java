package com.github.yuqingliu.extraenchants.view;

import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.managers.EventManager;
import com.github.yuqingliu.extraenchants.api.managers.MathManager;
import com.github.yuqingliu.extraenchants.api.managers.SoundManager;
import com.github.yuqingliu.extraenchants.api.view.PlayerInventory;
import com.github.yuqingliu.extraenchants.api.Scheduler;
import com.google.inject.Inject;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

@Getter
public abstract class AbstractPlayerInventory implements PlayerInventory {
    protected final int inventoryLength = 9;
    protected EventManager eventManager;
    protected final SoundManager soundManager;
    protected final Logger logger;
    protected final MathManager mathManager;
    @Setter protected Component displayName;
    protected final int inventorySize;
    protected final Component unavailableComponent = Component.text("Unavailable", NamedTextColor.DARK_PURPLE);
    protected final Component loadingComponent = Component.text("Loading...", NamedTextColor.RED);
    protected Map<Material, ItemStack> backgroundItems = new HashMap<>();
    protected ItemStack nextPage;
    protected ItemStack prevPage;
    protected ItemStack prevMenu;
    protected ItemStack exitMenu;
    protected ItemStack reload;
    protected ItemStack unavailable;
    protected ItemStack loading;
    protected ItemStack enchantingTable;
    protected ItemStack grindStone;
    protected ItemStack anvil;
        
    @Inject
    public AbstractPlayerInventory(EventManager eventManager, MathManager mathManager, SoundManager soundManager, Logger logger, Component displayName, int inventorySize) {
        this.eventManager = eventManager;
        this.mathManager = mathManager;
        this.soundManager = soundManager;
        this.logger = logger;
        this.displayName = displayName;
        this.inventorySize = inventorySize;
        initializeBackgroundItems();
        initializeCommonItems();
    }

    private void initializeBackgroundItems() {
        for(Material material : Material.values()) {
            if(material.name().contains("STAINED_GLASS_PANE")) {
                backgroundItems.put(material, createSlotItem(material, unavailableComponent));
            }
        }
    }

    private void initializeCommonItems() {
        this.nextPage = createSlotItem(Material.ARROW, Component.text("Next Page", NamedTextColor.AQUA));
        this.prevPage = createSlotItem(Material.ARROW, Component.text("Previous Page", NamedTextColor.AQUA));
        this.prevMenu = createSlotItem(Material.GREEN_WOOL, Component.text("Previous Menu", NamedTextColor.GREEN));
        this.exitMenu = createSlotItem(Material.RED_WOOL, Component.text("Exit", NamedTextColor.RED));
        this.reload = createSlotItem(Material.YELLOW_WOOL, Component.text("Refresh", NamedTextColor.YELLOW));
        this.unavailable = createSlotItem(Material.GLASS_PANE, unavailableComponent);
        this.loading = createSlotItem(Material.BARRIER, loadingComponent);
        this.enchantingTable = createSlotItem(Material.ENCHANTING_TABLE, Component.text("Enchanting Table", NamedTextColor.DARK_PURPLE));
        this.grindStone = createSlotItem(Material.GRINDSTONE, Component.text("Grindstone", NamedTextColor.GRAY));
        this.anvil = createSlotItem(Material.ANVIL, Component.text("Anvil", NamedTextColor.DARK_GRAY));
    }

    public boolean isUnavailable(ItemStack item) {
        return item.isSimilar(loading) ||
        item.isSimilar(unavailable) ||
        backgroundItems.containsValue(item) ||
        item.isSimilar(enchantingTable) ||
        item.isSimilar(grindStone) ||
        item.isSimilar(anvil);
    }

    public ItemStack createSlotItem(Material material, Component displayName, List<Component> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if(meta != null) {
            meta.displayName(displayName);
            meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
            meta.lore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createSlotItem(Material material, Component displayName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if(meta != null) {
            meta.displayName(displayName);
            meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
            meta.lore(Collections.emptyList());
        }
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createSlotItem(Material material, Component displayName, Component lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if(meta != null) {
            meta.displayName(displayName);
            meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
            meta.lore(Arrays.asList(lore));
        }
        item.setItemMeta(meta);
        return item;
    }

    public int[] toCoords(int slot) {
        int x = slot % inventoryLength;
        int y = slot / inventoryLength;
        return new int[] { x, y };
    }

    public int toIndex(int[] coords) {
        return coords[0] + (coords[1] * inventoryLength);
    }

    public ItemStack getItem(Inventory inv, int[] coords) {
        int index = toIndex(coords);
        return inv.getItem(index);
    }

    public void setItem(Inventory inv, int[] coords, ItemStack item) {
        inv.setItem(coords[0] + (coords[1] * inventoryLength), item);
    }

    public void setItem(Inventory inv, List<Integer> coords, ItemStack item) {
        inv.setItem(coords.get(0) + (coords.get(1) * inventoryLength), item);
    }

    public void clear(Inventory inv) {
        for (int i = 0; i < inventorySize; i++) {
            inv.setItem(i, new ItemStack(Material.AIR));
        }
    }

    public void fill(Inventory inv, ItemStack background) {
        for (int i = 0; i < inventorySize; i++) {
            inv.setItem(i, background);
        }
    }

    public boolean rectangleContains(int[] coords, List<int[]> rectangle) {
        return rectangle.stream().anyMatch(coord -> Arrays.equals(coord, coords));
    }

    public List<int[]> rectangleArea(int[] start, int width, int length) {
        List<int[]> results = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                int[] current = new int[]{start[0] + j, start[1] + i};
                results.add(current);
            }
        }
        return results;
    }

    public int rectangleIndex(int[] coords, List<int[]> rectangle) {
        int index = 0;
        for(int[] coord : rectangle) {
            if(Arrays.equals(coord, coords)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public void fillRectangleArea(Inventory inv, int[] start, int width, int length, ItemStack item) {
        List<int[]> rectangleCoords = rectangleArea(start, width, length);
        for(int[] coords : rectangleCoords) {
            setItem(inv, coords, item);
        }
    }

    public void rectangleAreaLoading(Inventory inv, int[] start, int width, int length) {
        List<int[]> rectangleCoords = rectangleArea(start, width, length);
        setItem(inv, rectangleCoords.get(0), loading);
        for (int i = 1; i < rectangleCoords.size(); i++) {
            setItem(inv, rectangleCoords.get(i), unavailable);
        }
    }

    public void addItemToPlayer(Player player, ItemStack item, int quantity) {
        int required = quantity;
        int maxStackSize = item.getType().getMaxStackSize();
        while(required > 0) {
            int substractedAmount = Math.min(required, maxStackSize);
            item.setAmount(substractedAmount);
            if (!player.getInventory().addItem(item).isEmpty()) {
                Scheduler.runLater((task) -> {
                    Location location = player.getLocation();
                    player.getWorld().dropItemNaturally(location, item);
                }, Duration.ofSeconds(0));
            }
            required -= substractedAmount;
        }
    }

    public boolean removeItemFromPlayer(Player player, ItemStack item, int quantity) {
        int totalItemCount = countItemFromPlayer(player, item);
        if (totalItemCount < quantity) {
            return false;
        }
        int remaining = quantity;
        for (ItemStack inventoryItem : player.getInventory().getContents()) {
            if (inventoryItem != null && inventoryItem.isSimilar(item)) {
                int amount = inventoryItem.getAmount();
                if (amount >= remaining) {
                    inventoryItem.setAmount(amount - remaining);
                    return true;
                } else {
                    inventoryItem.setAmount(0);
                    remaining -= amount;
                }
            }
        }
        return false;
    }

    public int countItemFromPlayer(Player player, ItemStack item) {
        int count = 0;
        for (ItemStack inventoryItem : player.getInventory().getContents()) {
            if (inventoryItem != null && inventoryItem.isSimilar(item)) {
                count += inventoryItem.getAmount();
            }
        }
        return count;
    }

    public int countAvailableInventorySpace(Player player, Material material) {
        Inventory inventory = player.getInventory();
        int maxStackSize = material.getMaxStackSize();
        int availableSpace = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null || item.getType() == Material.AIR) {
                availableSpace += maxStackSize;
            } else if (item.getType() == material) {
                availableSpace += maxStackSize - item.getAmount();
            }
        }
        return availableSpace;  
    }

    @Override
    public abstract Inventory load(Player player);

    @Override
    public abstract void open(Player player);
}
