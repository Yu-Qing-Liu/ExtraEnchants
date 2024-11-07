package com.github.yuqingliu.extraenchants.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.item.ItemImpl;
import com.google.inject.Singleton;

import lombok.Getter;

@Getter
@Singleton
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<ItemCategory, Set<Item>> items = new HashMap<>();
    
    public ItemRepositoryImpl() {
        initializeItems();
    }

    private void initializeItems() {
        items.put(ItemCategory.BOW,
            Set.of(
                new ItemImpl(new ItemStack(Material.BOW))
            )
        );

        items.put(ItemCategory.RANGED,
            Set.of(
                new ItemImpl(new ItemStack(Material.BOW)),
                new ItemImpl(new ItemStack(Material.CROSSBOW))
            )
        );
    }
}
