package com.github.yuqingliu.extraenchants.managers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.sisu.PostConstruct;

import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.managers.InventoryManager;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.view.PlayerInventory;
import com.github.yuqingliu.extraenchants.view.AbstractPlayerInventory;
import com.github.yuqingliu.extraenchants.view.enchantmenu.EnchantMenu;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@Singleton
public class InventoryManagerImpl implements InventoryManager {
    private final Logger logger;
    private Map<String, AbstractPlayerInventory> inventories = new HashMap<>();
    private ManagerRepository managerRepository;
    private EnchantmentRepository enchantmentRepository;
    
    @Inject
    public InventoryManagerImpl(Logger logger, ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository) {
        this.logger = logger;
        this.managerRepository = managerRepository;
        this.enchantmentRepository = enchantmentRepository;
    }
    
    @Override
    public void postConstruct() {
        inventories.put(
            EnchantMenu.class.getSimpleName(),
            new EnchantMenu(
                managerRepository, enchantmentRepository, logger,
                Component.text("Enchanting Table", NamedTextColor.DARK_PURPLE)
            )
        );       
    }

    @Override
    public PlayerInventory getInventory(String className) {
        return inventories.get(className);
    }
}
