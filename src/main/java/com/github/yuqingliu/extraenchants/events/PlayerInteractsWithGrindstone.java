package com.github.yuqingliu.extraenchants.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.Block;

import com.github.yuqingliu.extraenchants.api.managers.InventoryManager;
import com.github.yuqingliu.extraenchants.view.grindstonemenu.GrindstoneMenu;
import com.google.inject.Inject;

import lombok.RequiredArgsConstructor;

import org.bukkit.event.block.Action;

@RequiredArgsConstructor
public class PlayerInteractsWithGrindstone implements Listener {
    @Inject
    private final InventoryManager inventoryManager;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block != null && block.getType() == Material.GRINDSTONE) {
            event.setCancelled(true);
            inventoryManager.getInventory(GrindstoneMenu.class.getSimpleName()).open(player, block.getLocation());
        }
    }
}
