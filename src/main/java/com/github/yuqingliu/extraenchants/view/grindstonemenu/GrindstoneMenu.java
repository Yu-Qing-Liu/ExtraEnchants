package com.github.yuqingliu.extraenchants.view.grindstonemenu;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.repositories.AnvilRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.view.AbstractPlayerInventory;
import com.github.yuqingliu.extraenchants.view.grindstonemenu.mainmenu.MainMenu;

import lombok.Getter;
import net.kyori.adventure.text.Component;

@Getter
public class GrindstoneMenu extends AbstractPlayerInventory {
    private Map<Player, MenuType> playerMenuTypes = new ConcurrentHashMap<>();
    private final EnchantmentRepository enchantmentRepository;

    private final MainMenu mainMenu;

    public enum MenuType {
        MainMenu;
    }

    public GrindstoneMenu(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, Logger logger, Component displayName) {
        super(managerRepository, logger, displayName, 54);
        this.enchantmentRepository = enchantmentRepository;
        this.mainMenu = new MainMenu(this);
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
        mainMenu.getController().openMenu(player, inventory);
    }
}
