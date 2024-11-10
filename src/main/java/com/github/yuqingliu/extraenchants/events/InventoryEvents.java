package com.github.yuqingliu.extraenchants.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.Block;

import com.github.yuqingliu.extraenchants.api.managers.ConfigurationManager;
import com.github.yuqingliu.extraenchants.api.managers.InventoryManager;
import com.github.yuqingliu.extraenchants.api.repositories.CustomBlockRepository;
import com.github.yuqingliu.extraenchants.view.anvilmenu.AnvilMenu;
import com.github.yuqingliu.extraenchants.view.enchantmenu.EnchantMenu;
import com.github.yuqingliu.extraenchants.view.grindstonemenu.GrindstoneMenu;
import com.google.inject.Inject;

import lombok.RequiredArgsConstructor;

import org.bukkit.event.block.Action;

@RequiredArgsConstructor
public class InventoryEvents implements Listener {
    @Inject
    private final InventoryManager inventoryManager;
    @Inject
    private final CustomBlockRepository blockRepository;
    @Inject
    private final ConfigurationManager configurationManager;

    @EventHandler
    public void onAnvilInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block != null && block.getType() == Material.ANVIL) {
            if(configurationManager.enableAnvil() || blockRepository.getCustomBlocks().contains(block.getLocation())) {
                event.setCancelled(true);
                inventoryManager.getInventory(AnvilMenu.class.getSimpleName()).open(player, block.getLocation());
            }
        }
    }

    @EventHandler
    public void onEtableInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block != null && block.getType() == Material.ENCHANTING_TABLE) {
            if(configurationManager.enableEtable() || blockRepository.getCustomBlocks().contains(block.getLocation())) {
                event.setCancelled(true);
                inventoryManager.getInventory(EnchantMenu.class.getSimpleName()).open(player, block.getLocation());
            }
        }
    }

    @EventHandler
    public void onGrindstoneInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block != null && block.getType() == Material.GRINDSTONE) {
            if(configurationManager.enableGrindstone() || blockRepository.getCustomBlocks().contains(block.getLocation())) {
                event.setCancelled(true);
                inventoryManager.getInventory(GrindstoneMenu.class.getSimpleName()).open(player, block.getLocation());
            }
        }
    }
}
