package com.github.yuqingliu.extraenchants.events;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.block.Block;

import com.github.yuqingliu.extraenchants.api.managers.InventoryManager;
import com.github.yuqingliu.extraenchants.view.anvilmenu.AnvilMenu;
import com.google.inject.Inject;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.event.block.Action;

@RequiredArgsConstructor
public class PlayerInteractsWithAnvil implements Listener {
    @Inject
    private final InventoryManager inventoryManager;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block != null && block.getType() == Material.ANVIL) {
            event.setCancelled(true);
            ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
            ItemMeta meta = item.getItemMeta();
            meta.lore(Arrays.asList(Component.text("Some Lore", NamedTextColor.WHITE)));
            item.setItemMeta(meta);
            Inventory playerInventory = player.getInventory();
            playerInventory.addItem(item);
            inventoryManager.getInventory(AnvilMenu.class.getSimpleName()).open(player);
        }
    }
}
