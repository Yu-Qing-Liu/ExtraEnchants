package com.github.yuqingliu.extraenchants.repositories;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.api.persistence.Database;
import com.github.yuqingliu.extraenchants.api.repositories.AnvilRepository;
import com.github.yuqingliu.extraenchants.item.ItemImpl;
import com.github.yuqingliu.extraenchants.persistence.anvil.AnvilDTO;
import com.github.yuqingliu.extraenchants.persistence.anvil.AnvilDatabase;

import lombok.Getter;

@Getter
public class AnvilRepositoryImpl implements AnvilRepository {
    private AnvilDatabase anvilDatabase;
    private final Map<Item, Set<Item>> anvilCombinations = new HashMap<>();
    
    public AnvilRepositoryImpl() {
        initializeCombinations();
    }

    @Override
    public void setDatabase(Database database) {
        this.anvilDatabase = (AnvilDatabase) database;
    }

    @Override
    public void addCombination(Item item1, Item item2) {
        anvilCombinations.get(item1).add(item2);
        anvilCombinations.get(item2).add(item1);
        File item1File = anvilDatabase.getItemFile(item1);
        File item2File = anvilDatabase.getItemFile(item2);
        Set<Item> item1Combinable = anvilCombinations.get(item1);
        Set<Item> item2Combinable = anvilCombinations.get(item2);
        AnvilDTO data1 = new AnvilDTO(item1, item1Combinable);
        AnvilDTO data2 = new AnvilDTO(item2, item2Combinable);
        anvilDatabase.writeAsyncObject(item1File, data1);
        anvilDatabase.writeAsyncObject(item2File, data2);
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
