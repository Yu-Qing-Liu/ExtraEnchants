package com.github.yuqingliu.extraenchants.api.repositories;

import java.util.Map;
import java.util.Set;

import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.api.persistence.Database;

public interface AnvilRepository {
    Map<Item, Set<Item>> getAnvilCombinations();
    void setDatabase(Database database);
    void addCombination(Item item1, Item item2);
}
