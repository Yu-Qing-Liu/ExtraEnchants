package com.github.yuqingliu.extraenchants.view.enchantmenu;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.managers.EventManager;
import com.github.yuqingliu.extraenchants.api.managers.SoundManager;
import com.github.yuqingliu.extraenchants.view.AbstractPlayerInventory;

import lombok.Getter;
import net.kyori.adventure.text.Component;

@Getter
public class EnchantMenu extends AbstractPlayerInventory {
    private Map<Player, MenuType> playerMenuTypes = new ConcurrentHashMap<>();

    public enum MenuType {
        MainMenu;
    }

    public EnchantMenu(EventManager eventManager, SoundManager soundManager, Logger logger, Component displayName) {
        super(eventManager, soundManager, logger, displayName, 54);
    }

    @Override
    public Inventory load(Player player) {
        Inventory inventory = Bukkit.createInventory(null, inventorySize, displayName);
        player.openInventory(inventory);
        return inventory;
    }

    @Override
    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, inventorySize, displayName);
        player.openInventory(inventory);
    }

    
}
