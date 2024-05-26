package com.github.yuqingliu.extraenchants.api.item.lore;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface LoreManager {
    void addSection(String sectionName, LoreSection section);

    @NotNull ItemStack applyLore();
}
