package com.github.yuqingliu.extraenchants.persistence.blocks.threads;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.github.yuqingliu.extraenchants.persistence.blocks.CustomBlock;

public class AddCustomBlockTask implements Runnable {
    private CustomBlock block;
    private File dataFile;

    public AddCustomBlockTask(CustomBlock block, File dataFile) {
        this.block = block;
        this.dataFile = dataFile;
    }

    @Override
    public void run() {
        // Ensure directory exists
        dataFile.getParentFile().mkdirs();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile, true));
            String line = block.toString();
            writer.write(line);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
