package com.github.yuqingliu.extraenchants.api.managers;

import com.github.yuqingliu.extraenchants.api.view.PlayerInventory;

public interface InventoryManager {
    PlayerInventory getInventory(String className);
    void initialize();
}
