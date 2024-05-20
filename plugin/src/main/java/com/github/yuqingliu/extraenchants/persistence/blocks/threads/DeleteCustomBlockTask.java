package com.github.yuqingliu.extraenchants.persistence.blocks.threads;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.github.yuqingliu.extraenchants.persistence.blocks.CustomBlock;

public class DeleteCustomBlockTask implements Runnable {
    private CustomBlock block;
    private File dataFile;

    public DeleteCustomBlockTask(CustomBlock block, File dataFile) {
        this.block = block;
        this.dataFile = dataFile;
    }

    @Override
    public void run() {
        File tempFile = new File(dataFile.getAbsolutePath() + ".tmp");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            // Ensure directory exists
            dataFile.getParentFile().mkdirs();

            // Reading and writing header
            String header = reader.readLine();
            if(header != null) {
                writer.write(header + System.lineSeparator());
            }
            
            String currentLine;
            String deleteLine = block.toString();

            while((currentLine = reader.readLine()) != null) {
                currentLine += "\n";
                if(currentLine.equals(deleteLine)) continue;
                writer.write(currentLine);
            }
            
            reader.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (!dataFile.delete()) {
            System.out.println("Could not delete the original file");
        }
        if (!tempFile.renameTo(dataFile)) {
            System.out.println("Could not rename the temp file to the original file name");
        }
    }
}
