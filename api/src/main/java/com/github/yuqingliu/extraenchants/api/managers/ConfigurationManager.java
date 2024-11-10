package com.github.yuqingliu.extraenchants.api.managers;

public interface ConfigurationManager {
    <T> T setConstant(String key, T value, Class<T> clazz);
    boolean enableEtable();
    boolean enableAnvil();
    boolean enableGrindstone();
    double getAnvilRepairCost();
    double getAnvilUpgradeCost();
}
