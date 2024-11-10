package com.github.yuqingliu.extraenchants.persistence.blocks;

import java.io.File;

import com.github.yuqingliu.extraenchants.api.repositories.CustomBlockRepository;
import com.github.yuqingliu.extraenchants.persistence.AbstractDatabase;
import com.google.inject.Inject;

import lombok.Getter;

@Getter
public class CustomBlockDatabase extends AbstractDatabase {
    private final CustomBlockRepository blockRepository;
    private final File blocksDirectory = new File(rootDirectory, "customblocks/");
    private final File blocksFile = new File(blocksDirectory, "BLOCKS.json");

    @Inject
    public CustomBlockDatabase(File rootDirectory, CustomBlockRepository blockRepository) {
        super(rootDirectory);
        this.blockRepository = blockRepository;
    }

    @Inject
    void postConstruct() {
        try {
            if (!blocksDirectory.exists()) {
                blocksDirectory.mkdir();
            }
            File[] files = blocksDirectory.listFiles();
            if (files.length > 0) {
                // Update data
                BlocksDTO blocks = readObject(blocksFile, BlocksDTO.class);
                blockRepository.getCustomBlocks().addAll(blocks.getBlocks());
            } else {
                // Populate with defaults
                BlocksDTO blocks = new BlocksDTO(blockRepository.getCustomBlocks());
                writeObject(blocksFile, blocks);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
