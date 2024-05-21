package com.github.yuqingliu.extraenchants.persistence.blocks;

import java.util.*;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CustomBlockDatabase {
    // [worldName, list of custom blocks in that world]
    protected static HashMap<String, Set<CustomBlock>> blocks = new HashMap<>(); 
    
    // IO Thread pool
    protected static ExecutorService threadPool;
    
    // blocks.csv File Persists custom block data
    public static File customBlocksFile;

    public static void start(File customBlocksFile) {
        CustomBlockDatabase.customBlocksFile = customBlocksFile;
        loadCustomBlocks(customBlocksFile);
        threadPool = Executors.newSingleThreadExecutor();
    }

    public static void stop() {
        // Initiate shutdown, allowing submitted tasks to complete
        threadPool.shutdown();
        try {
            // Wait a certain amount of time for existing tasks to terminate
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                // Attempt to stop all actively executing tasks and return a list of those that were awaiting execution.
                threadPool.shutdownNow();

                // Wait again, after calling shutdownNow, to ensure tasks are terminated
                if (!threadPool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Thread pool did not terminate.");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            threadPool.shutdownNow();

            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }       
    }

    public static void loadCustomBlocks(File file) {
        // Clear the current map to ensure it's fresh on reload.
        blocks.clear();

        // Ensure directory exists
        if(!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                try {
                    FileWriter writer = new FileWriter(file);
                    writer.write("worldName,x,y,z,blockType\n"); // Write the header 
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            // Read and ignore the header
            br.readLine();
            // Upload data
            while((line = br.readLine()) != null) {
                CustomBlock b = new CustomBlock(line);
                String worldName = b.getWorldName();
                if(blocks.containsKey(worldName)) {
                    blocks.get(worldName).add(b);
                } else {
                    Set<CustomBlock> l = new HashSet<>();
                    l.add(b);
                    blocks.put(b.getWorldName(), l);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
