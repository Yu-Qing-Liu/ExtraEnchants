package com.github.yuqingliu.extraenchants.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.managers.InventoryManager;
import com.github.yuqingliu.extraenchants.api.managers.NameSpacedKeyManager;
import com.github.yuqingliu.extraenchants.api.repositories.CustomBlockRepository;
import com.github.yuqingliu.extraenchants.view.anvilmenu.AnvilMenu;
import com.github.yuqingliu.extraenchants.view.enchantmenu.EnchantMenu;
import com.github.yuqingliu.extraenchants.view.grindstonemenu.GrindstoneMenu;
import com.google.inject.Inject;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WandEvents implements Listener {
    @Inject
    private final Logger logger;
    @Inject
    private final CustomBlockRepository blockRepository;
    @Inject
    private final NameSpacedKeyManager nameSpacedKeyManager;
    @Inject
    private final InventoryManager inventoryManager;

    @EventHandler
    public void onAnvilInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block != null && block.getType() == Material.ANVIL) {
            ItemStack mainHandItem = player.getInventory().getItemInMainHand();
            ItemMeta meta = mainHandItem.getItemMeta();
            if(meta == null) {
                return;
            }
            PersistentDataContainer container = meta.getPersistentDataContainer();
            if(container.has(nameSpacedKeyManager.getCustomUIKey())) {
                if(container.get(nameSpacedKeyManager.getCustomUIKey(), PersistentDataType.STRING).equals(AnvilMenu.class.getSimpleName())) {
                    event.setCancelled(true);
                    blockRepository.addCustomBlock(block.getLocation());
                    logger.sendPlayerAcknowledgementMessage(player, String.format("Custom anvil set at %s", block.getLocation().toString()));
                }
            }
        }
    }
    
    @EventHandler
    public void onEtableInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block != null && block.getType() == Material.ENCHANTING_TABLE) {
            ItemStack mainHandItem = player.getInventory().getItemInMainHand();
            ItemMeta meta = mainHandItem.getItemMeta();
            if(meta == null) {
                return;
            }
            PersistentDataContainer container = meta.getPersistentDataContainer();
            if(container.has(nameSpacedKeyManager.getCustomUIKey())) {
                if(container.get(nameSpacedKeyManager.getCustomUIKey(), PersistentDataType.STRING).equals(EnchantMenu.class.getSimpleName())) {
                    event.setCancelled(true);
                    blockRepository.addCustomBlock(block.getLocation());
                    logger.sendPlayerAcknowledgementMessage(player, String.format("Custom enchanting table set at %s", block.getLocation().toString()));
                }
            }
        }
    }

    @EventHandler
    public void onGrindstoneInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block != null && block.getType() == Material.GRINDSTONE) {
            ItemStack mainHandItem = player.getInventory().getItemInMainHand();
            ItemMeta meta = mainHandItem.getItemMeta();
            if(meta == null) {
                return;
            }
            PersistentDataContainer container = meta.getPersistentDataContainer();
            if(container.has(nameSpacedKeyManager.getCustomUIKey())) {
                if(container.get(nameSpacedKeyManager.getCustomUIKey(), PersistentDataType.STRING).equals(GrindstoneMenu.class.getSimpleName())) {
                    event.setCancelled(true);
                    blockRepository.addCustomBlock(block.getLocation());
                    logger.sendPlayerAcknowledgementMessage(player, String.format("Custom grindstone set at %s", block.getLocation().toString()));
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if(blockRepository.getCustomBlocks().contains(block.getLocation())) {
            blockRepository.deleteCustomBlock(block.getLocation());
            switch (event.getBlock().getType()) {
                case ANVIL -> {
                    logger.sendPlayerErrorMessage(player, String.format("Custom anvil removed at %s", block.getLocation().toString()));
                }
                case ENCHANTING_TABLE -> {
                    logger.sendPlayerErrorMessage(player, String.format("Custom enchanting table removed at %s", block.getLocation().toString()));
                }
                case GRINDSTONE -> {
                    logger.sendPlayerErrorMessage(player, String.format("Custom grindstone removed at %s", block.getLocation().toString()));
                }
                default -> {
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();
        PersistentDataContainer container = entity.getPersistentDataContainer();
        if(container.has(nameSpacedKeyManager.getCustomUIKey())) {
            String uiName = container.get(nameSpacedKeyManager.getCustomUIKey(), PersistentDataType.STRING);
            switch (uiName) {
                case "AnvilMenu" -> {
                    inventoryManager.getInventory(AnvilMenu.class.getSimpleName()).open(player, entity.getLocation());
                }
                case "EnchantMenu" -> {
                    inventoryManager.getInventory(EnchantMenu.class.getSimpleName()).open(player, entity.getLocation());
                }
                case "GrindstoneMenu" -> {
                    inventoryManager.getInventory(GrindstoneMenu.class.getSimpleName()).open(player, entity.getLocation());
                }
                default -> {
                    return;
                }
            }
            return;
        }
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        ItemMeta meta = mainHandItem.getItemMeta();
        if(meta == null) {
            return;
        }
        PersistentDataContainer itemContainer = meta.getPersistentDataContainer();
        if(itemContainer.has(nameSpacedKeyManager.getCustomUIKey())) {
            String uiName = itemContainer.get(nameSpacedKeyManager.getCustomUIKey(), PersistentDataType.STRING);
            switch (uiName) {
                case "AnvilMenu" -> {
                    PersistentDataContainer entityContainer = entity.getPersistentDataContainer();
                    entityContainer.set(nameSpacedKeyManager.getCustomUIKey(), PersistentDataType.STRING, AnvilMenu.class.getSimpleName());
                    entity.setPersistent(true);
                    logger.sendPlayerAcknowledgementMessage(player, String.format("Entity %s is now a custom anvil", entity.getName()));
                }
                case "EnchantMenu" -> {
                    PersistentDataContainer entityContainer = entity.getPersistentDataContainer();
                    entityContainer.set(nameSpacedKeyManager.getCustomUIKey(), PersistentDataType.STRING, EnchantMenu.class.getSimpleName());
                    entity.setPersistent(true);
                    logger.sendPlayerAcknowledgementMessage(player, String.format("Entity %s is now a custom enchanting table", entity.getName()));
                }
                case "GrindstoneMenu" -> {
                    PersistentDataContainer entityContainer = entity.getPersistentDataContainer();
                    entityContainer.set(nameSpacedKeyManager.getCustomUIKey(), PersistentDataType.STRING, GrindstoneMenu.class.getSimpleName());
                    entity.setPersistent(true);
                    logger.sendPlayerAcknowledgementMessage(player, String.format("Entity %s is now a custom grindstone", entity.getName()));
                }
                default -> {
                    return;
                }
            }
        }
    }
}
