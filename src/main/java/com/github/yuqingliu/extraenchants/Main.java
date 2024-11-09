package com.github.yuqingliu.extraenchants;

import com.github.yuqingliu.extraenchants.api.ExtraEnchants;
import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.persistence.Database;
import com.github.yuqingliu.extraenchants.api.repositories.*;
import com.github.yuqingliu.extraenchants.modules.PluginModule;
import com.github.yuqingliu.extraenchants.persistence.anvil.AnvilDatabase;
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
    private ManagerRepository managerRepository;

    private Database enchantmentDatabase;
    private Database anvilDatabase;

    @Override
    public void onEnable() {
        injector = Guice.createInjector(new PluginModule(this));
        injector.injectMembers(this);
        extraenchantsLogger = injector.getInstance(Logger.class);

        managerRepository = injector.getInstance(ManagerRepository.class);
        enchantmentRepository = injector.getInstance(EnchantmentRepository.class);
        itemRepository = injector.getInstance(ItemRepository.class);
        postConstruct();

        enchantmentDatabase = injector.getInstance(EnchantmentDatabase.class);
        anvilDatabase = injector.getInstance(AnvilDatabase.class);
    }

    private void postConstruct() {
        managerRepository.getInventoryManager().postConstruct();
        enchantmentRepository.postConstruct();
    }
}
