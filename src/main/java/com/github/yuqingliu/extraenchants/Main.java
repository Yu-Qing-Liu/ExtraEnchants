package com.github.yuqingliu.extraenchants;

import com.github.yuqingliu.extraenchants.api.ExtraEnchants;
import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.managers.*;
import com.github.yuqingliu.extraenchants.api.persistence.Database;
import com.github.yuqingliu.extraenchants.api.repositories.*;
import com.github.yuqingliu.extraenchants.modules.PluginModule;
import com.github.yuqingliu.extraenchants.persistence.enchantments.EnchantmentDatabase;
import com.google.inject.Guice;
import com.google.inject.Injector;

import lombok.Getter;

@Getter
public class Main extends ExtraEnchants {
    private Injector injector;
    
    private Logger extraenchantsLogger;

    private ItemRepository itemRepository;
    private EnchantmentRepository enchantmentRepository;

    private Database enchantmentDatabase;

    private EventManager eventManager;
    private CommandManager commandManager;
    private InventoryManager inventoryManager;
    private NameSpacedKeyManager nameSpacedKeyManager;
    private SoundManager soundManager;
    private TextManager textManager;
    private ColorManager colorManager;
    private LoreManager loreManager;
    private MathManager mathManager;

    @Override
    public void onEnable() {
        injector = Guice.createInjector(new PluginModule(this));
        injector.injectMembers(this);
        extraenchantsLogger = injector.getInstance(Logger.class);
        eventManager = injector.getInstance(EventManager.class);
        commandManager = injector.getInstance(CommandManager.class);
        inventoryManager = injector.getInstance(InventoryManager.class);
        nameSpacedKeyManager = injector.getInstance(NameSpacedKeyManager.class);
        soundManager = injector.getInstance(SoundManager.class);
        textManager = injector.getInstance(TextManager.class);
        colorManager = injector.getInstance(ColorManager.class);
        loreManager = injector.getInstance(LoreManager.class);
        mathManager = injector.getInstance(MathManager.class);
        enchantmentRepository = injector.getInstance(EnchantmentRepository.class);
        itemRepository = injector.getInstance(ItemRepository.class);
        enchantmentDatabase = injector.getInstance(EnchantmentDatabase.class);

        inventoryManager.initialize(eventManager);
        enchantmentDatabase.initialize();
    }
}
