package com.github.yuqingliu.extraenchants.api.repositories;

import java.util.Set;
import com.github.yuqingliu.extraenchants.api.persistence.Database;

import org.bukkit.Location;

public interface CustomBlockRepository {
    Set<Location> getCustomBlocks(); 
    void addCustomBlock(Location location);
    void deleteCustomBlock(Location location);
    void setDatabase(Database database);
}
