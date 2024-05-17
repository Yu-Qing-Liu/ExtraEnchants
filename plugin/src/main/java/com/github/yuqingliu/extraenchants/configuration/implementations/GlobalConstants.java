package com.github.yuqingliu.extraenchants.configuration.implementations;

import com.github.yuqingliu.extraenchants.configuration.AbstractConstants;

import lombok.Getter;

import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class GlobalConstants extends AbstractConstants {
    private double repairAnvilCostPerResource = 1.5;
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
        repairAnvilCostPerResource = (double) setConstant("RepairAnvilCostPerResource", repairAnvilCostPerResource);
        applyVanillaEnchantingTableBehavior = (boolean) setConstant("ApplyVanillaEnchantingTableBehavior", applyVanillaEnchantingTableBehavior);
        applyVanillaEnchantingTableBehavior = (boolean) setConstant("ApplyVanillaGrindstoneTableBehavior", applyVanillaGrindstoneBehavior);
        applyVanillaEnchantingTableBehavior = (boolean) setConstant("ApplyVanillaAnvilBehavior", applyVanillaAnvilBehavior);
    }
}
