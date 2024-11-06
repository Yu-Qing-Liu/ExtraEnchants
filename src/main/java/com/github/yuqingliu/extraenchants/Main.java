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
import com.google.inject.Inject;
import com.google.inject.Injector;

import lombok.Getter;

@Getter
public class Main extends ExtraEnchants {
    private Injector injector;
    
    @Inject
    private Logger extraenchantsLogger;
    @Inject
    private EventManager eventManager;
    @Inject
    private CommandManager commandManager;
    @Inject
    private InventoryManager inventoryManager;
    @Inject
    private NameSpacedKeyManager nameSpacedKeyManager;
    @Inject
    private SoundManager soundManager;
    @Inject
    private TextManager textManager;
    @Inject
    private ColorManager colorManager;
    @Inject
    private LoreManager loreManager;

    @Override
    public void onEnable() {
        injector = Guice.createInjector(new PluginModule(this));
        injector.injectMembers(this);
    }

}
