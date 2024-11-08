package com.github.yuqingliu.extraenchants.api.repositories;

import java.util.Map;
import java.util.Set;

import com.github.yuqingliu.extraenchants.api.item.Item;

public interface AnvilRepository {
    Map<Item, Set<Item>> getAnvilCombinations();
}
