package com.github.yuqingliu.extraenchants.repositories;

import java.util.Set;

import org.bukkit.Location;

import com.github.yuqingliu.extraenchants.api.persistence.Database;
import com.github.yuqingliu.extraenchants.api.repositories.CustomBlockRepository;
import com.github.yuqingliu.extraenchants.persistence.blocks.BlocksDTO;
import com.github.yuqingliu.extraenchants.persistence.blocks.CustomBlockDatabase;
import com.google.common.collect.Sets;

import lombok.Getter;

@Getter
public class CustomBlockRepositoryImpl implements CustomBlockRepository {
    private CustomBlockDatabase blocksDatabase;
    Set<Location> customBlocks = Sets.newConcurrentHashSet();
    
    @Override
    public void postConstruct(Database blocksDatabase) {
        this.blocksDatabase = (CustomBlockDatabase) blocksDatabase;
    }

    @Override
    public void addCustomBlock(Location location) {
        customBlocks.add(location);
        BlocksDTO data = new BlocksDTO(customBlocks);
        blocksDatabase.writeAsyncObject(blocksDatabase.getBlocksFile(), data);
    }

    @Override
    public void deleteCustomBlock(Location location) {
        customBlocks.remove(location);
        BlocksDTO data = new BlocksDTO(customBlocks);
        blocksDatabase.writeAsyncObject(blocksDatabase.getBlocksFile(), data);
    }
}
