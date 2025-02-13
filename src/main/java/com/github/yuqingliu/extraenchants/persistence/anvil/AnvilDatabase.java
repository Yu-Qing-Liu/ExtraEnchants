package com.github.yuqingliu.extraenchants.persistence.anvil;

import java.io.File;
import java.util.Map;
import java.util.Set;

import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.api.repositories.AnvilRepository;
import com.github.yuqingliu.extraenchants.persistence.AbstractDatabase;
import com.google.inject.Inject;

public class AnvilDatabase extends AbstractDatabase {
    private final AnvilRepository anvilRepository;
    private final File anvilDirectory = new File(rootDirectory, "anvilcombinations/");
    
    @Inject
    public AnvilDatabase(File rootDirectory, AnvilRepository anvilRepository) {
        super(rootDirectory);
        this.anvilRepository = anvilRepository;
    }
    
    @Inject
    public void postConstruct() {
        try {
            if (!anvilDirectory.exists()) {
                anvilDirectory.mkdir();
            }
            File[] files = anvilDirectory.listFiles();
            if (files.length > 0) {
                for(File anvilFile : files) {
                    AnvilDTO data = readObject(anvilFile, AnvilDTO.class);
                    Item key = data.getItem();
                    Set<Item> value = data.getCombinable();
                    anvilRepository.getAnvilCombinations().put(key, value);
                }
            } 
            for(Map.Entry<Item, Set<Item>> entry : anvilRepository.getAnvilCombinations().entrySet()) {
                File anvilFile = new File(anvilDirectory, entry.getKey().getDisplayName() + ".json");
                if(!anvilFile.exists()) {
                    AnvilDTO data = new AnvilDTO(entry.getKey(), entry.getValue());
                    writeObject(anvilFile, data);
                }
            }   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getItemFile(Item item) {
        return new File(anvilDirectory, item.getDisplayName() + ".json");
    }
}
