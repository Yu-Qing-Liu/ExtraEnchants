package com.github.yuqingliu.extraenchants.configuration.implementations;

import com.github.yuqingliu.extraenchants.configuration.AbstractConstants;

import lombok.Getter;

import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class AnvilConstants extends AbstractConstants {
    private double anvilRepairCostPerResource = 1.5;
    private double anvilCostPerLevel = 2;

    public AnvilConstants(JavaPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public void registerConstants() {
        anvilRepairCostPerResource = (double) setConstant("RepairAnvilCostPerResource", anvilRepairCostPerResource);
        anvilCostPerLevel = (double) setConstant("RepairAnvilCostPerLevel", anvilCostPerLevel);
    }
}
