package com.github.yuqingliu.extraenchants.repositories;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.api.repositories.AnvilRepository;
import com.github.yuqingliu.extraenchants.item.ItemImpl;

import lombok.Getter;

@Getter
public class AnvilRepositoryImpl implements AnvilRepository {
    private final Map<Item, Set<Item>> anvilCombinations = new HashMap<>();
    
    public AnvilRepositoryImpl() {
        initializeCombinations();
    }

    private void initializeCombinations() {
        for(Material m : Material.values()) {
            try {
                Item i = new ItemImpl(new ItemStack(m));
                anvilCombinations.put(i, new HashSet<>());
            } catch (Exception e) {}
        }
    }
}
