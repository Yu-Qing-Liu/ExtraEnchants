package com.github.yuqingliu.extraenchants;

import com.github.yuqingliu.extraenchants.api.ExtraEnchants;
import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.managers.ColorManager;
import com.github.yuqingliu.extraenchants.api.managers.CommandManager;
import com.github.yuqingliu.extraenchants.api.managers.EventManager;
import com.github.yuqingliu.extraenchants.api.managers.InventoryManager;
import com.github.yuqingliu.extraenchants.api.managers.LoreManager;
import com.github.yuqingliu.extraenchants.api.managers.NameSpacedKeyManager;
import com.github.yuqingliu.extraenchants.api.managers.SoundManager;
import com.github.yuqingliu.extraenchants.api.managers.TextManager;
import com.github.yuqingliu.extraenchants.modules.PluginModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import lombok.Getter;

@Getter
public class Main extends ExtraEnchants {
    private Injector injector;
    
    private Logger extraenchantsLogger;
    private EventManager eventManager;
    private CommandManager commandManager;
    private InventoryManager inventoryManager;
    private NameSpacedKeyManager nameSpacedKeyManager;
    private SoundManager soundManager;
    private TextManager textManager;
    private ColorManager colorManager;
    private LoreManager loreManager;

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

        inventoryManager.initialize(eventManager);
    }
}
