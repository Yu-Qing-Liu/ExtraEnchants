package com.github.yuqingliu.extraenchants.api.repositories;

import java.util.Map;
import java.util.Set;

import com.github.yuqingliu.extraenchants.api.item.Item;

public interface ItemRepository {
    public enum ItemCategory {
        WEAPON, RANGED, MELEE, SWORD, AXE, BOW, CROSSBOW, HAMMER, ARMOR, HEMLET, CHESTPLATE, LEGGING, BOOT, TOOL, PICKAXE, SHOVEL, HOE, ELYTRA, ALL;  
    }

    Map<ItemCategory, Set<Item>> getItems();   
}
