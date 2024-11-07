package com.github.yuqingliu.extraenchants.view.enchantmenu;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.managers.EventManager;
import com.github.yuqingliu.extraenchants.api.managers.MathManager;
import com.github.yuqingliu.extraenchants.api.managers.SoundManager;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.view.AbstractPlayerInventory;
import com.github.yuqingliu.extraenchants.view.enchantmenu.mainmenu.MainMenu;
import com.github.yuqingliu.extraenchants.view.enchantmenu.offermenu.OfferMenu;

import lombok.Getter;
import net.kyori.adventure.text.Component;

@Getter
public class EnchantMenu extends AbstractPlayerInventory {
    private Map<Player, MenuType> playerMenuTypes = new ConcurrentHashMap<>();
    private final EnchantmentRepository enchantmentRepository;

    private final MainMenu mainMenu;
    private final OfferMenu offerMenu;

    public enum MenuType {
        MainMenu, OfferMenu;
    }

    public EnchantMenu(EventManager eventManager, MathManager mathManager, SoundManager soundManager, Logger logger, Component displayName, EnchantmentRepository enchantmentRepository) {
        super(eventManager, mathManager, soundManager, logger, displayName, 54);
        this.enchantmentRepository = enchantmentRepository;
        this.mainMenu = new MainMenu(this);
        this.offerMenu = new OfferMenu(this);
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
