package com.github.yuqingliu.extraenchants.configuration.implementations;

import com.github.yuqingliu.extraenchants.configuration.AbstractConstants;

import org.bukkit.plugin.java.JavaPlugin;

public class GlobalConstants extends AbstractConstants {
    private boolean applyVanillaEnchantingTableBehavior = false;
    private boolean applyVanillaGrindstoneBehavior = false;
    private boolean applyVanillaAnvilBehavior = false;

    public GlobalConstants(JavaPlugin plugin) {
        super(plugin);
    }
    
    public boolean getApplyVanillaEnchantingTableBehavior() {
        return this.applyVanillaEnchantingTableBehavior;  
    }

    public boolean getApplyVanillaGrindstoneBehavior() {
        return this.applyVanillaGrindstoneBehavior;  
    }

    public boolean getApplyVanillaAnvilBehavior() {
        return this.applyVanillaAnvilBehavior;  
    }

    @Override
    public void registerConstants() {
        applyVanillaEnchantingTableBehavior = (boolean) setConstant("ApplyVanillaEnchantingTableBehavior", applyVanillaEnchantingTableBehavior);
        applyVanillaEnchantingTableBehavior = (boolean) setConstant("ApplyVanillaGrindstoneTableBehavior", applyVanillaGrindstoneBehavior);
        applyVanillaEnchantingTableBehavior = (boolean) setConstant("ApplyVanillaAnvilBehavior", applyVanillaAnvilBehavior);
    }
}
