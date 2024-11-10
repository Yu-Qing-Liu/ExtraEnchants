package com.github.yuqingliu.extraenchants.api.view;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.kyori.adventure.text.Component;

public interface PlayerInventory {
    void open(Player player, Location location);
    Inventory load(Player player);
    void setDisplayName(Component displayName);
    Location getLocation(Player player);
}
