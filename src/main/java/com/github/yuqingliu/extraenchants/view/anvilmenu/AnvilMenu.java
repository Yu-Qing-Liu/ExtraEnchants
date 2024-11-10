package com.github.yuqingliu.extraenchants.view.anvilmenu;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.repositories.AnvilRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.view.AbstractPlayerInventory;
import com.github.yuqingliu.extraenchants.view.anvilmenu.mainmenu.MainMenu;

import lombok.Getter;
import net.kyori.adventure.text.Component;

@Getter
public class AnvilMenu extends AbstractPlayerInventory {
    private Map<Player, MenuType> playerMenuTypes = new ConcurrentHashMap<>();
    private final EnchantmentRepository enchantmentRepository;
    private final AnvilRepository anvilRepository;

    private final MainMenu mainMenu;

    public enum MenuType {
        MainMenu;
    }

    public AnvilMenu(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, AnvilRepository anvilRepository, Logger logger, Component displayName) {
        super(managerRepository, logger, displayName, 36);
        this.enchantmentRepository = enchantmentRepository;
        this.anvilRepository = anvilRepository;
        this.mainMenu = new MainMenu(this);
    }

    @Override
    public Inventory load(Player player) {
        Inventory inventory = Bukkit.createInventory(null, inventorySize, displayName);
        player.openInventory(inventory);
        return inventory;
    }

    @Override
    public void open(Player player, Location location) {
        inventoryLocations.put(player, location);
        Inventory inventory = Bukkit.createInventory(null, inventorySize, displayName);
        player.openInventory(inventory);
        mainMenu.getController().openMenu(player, inventory);
    }
}
