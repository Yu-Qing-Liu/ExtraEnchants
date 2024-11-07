package com.github.yuqingliu.extraenchants.managers;

import java.util.HashMap;
import java.util.Map;

import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.managers.EventManager;
import com.github.yuqingliu.extraenchants.api.managers.InventoryManager;
import com.github.yuqingliu.extraenchants.api.managers.SoundManager;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.view.PlayerInventory;
import com.github.yuqingliu.extraenchants.view.AbstractPlayerInventory;
import com.github.yuqingliu.extraenchants.view.enchantmenu.EnchantMenu;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@Singleton
public class InventoryManagerImpl implements InventoryManager {
    private final SoundManager soundManager;
    private final Logger logger;
    private EnchantmentRepository enchantmentRepository;
    private Map<String, AbstractPlayerInventory> inventories = new HashMap<>();
    
    @Inject
    public InventoryManagerImpl(SoundManager soundManager, Logger logger, EnchantmentRepository enchantmentRepository) {
        this.soundManager = soundManager;
        this.logger = logger;
        this.enchantmentRepository = enchantmentRepository;
    }
    
    @Override
    public void initialize(EventManager eventManager) {
        inventories.put(
            EnchantMenu.class.getSimpleName(),
            new EnchantMenu(
                eventManager, soundManager, logger,
                Component.text("Enchanting Table", NamedTextColor.DARK_PURPLE),
                enchantmentRepository
            )
        );       
    }

    @Override
    public PlayerInventory getInventory(String className) {
        return inventories.get(className);
    }
}
