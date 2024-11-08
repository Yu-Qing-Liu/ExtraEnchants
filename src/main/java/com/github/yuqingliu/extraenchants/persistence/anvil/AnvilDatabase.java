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
    private final File anvilFile = new File(anvilDirectory, "ANVILDATA.json");
    
    @Inject
    public AnvilDatabase(File rootDirectory, AnvilRepository anvilRepository) {
        super(rootDirectory);
        this.anvilRepository = anvilRepository;
    }

    @Override
    public void postConstruct() {
        try {
            if (!anvilDirectory.exists()) {
                anvilDirectory.mkdir();
            }
            File[] files = anvilDirectory.listFiles();
            if (files.length > 0) {
                // Update data
                AnvilDTO data = readObject(anvilFile, AnvilDTO.class);
                mergeData(data, anvilRepository.getAnvilCombinations());
            } else {
                // Populate with defaults
                AnvilDTO data = new AnvilDTO(anvilRepository.getAnvilCombinations());
                writeObject(anvilFile, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mergeData(AnvilDTO data, Map<Item, Set<Item>> anvilCombinations) {
        anvilCombinations.putAll(data.getAnvilCombinations());
    }
}
