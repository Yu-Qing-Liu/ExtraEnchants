package com.github.yuqingliu.extraenchants.persistence.enchantments;

import java.io.File;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.persistence.AbstractDatabase;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class EnchantmentDatabase extends AbstractDatabase {
    private final EnchantmentRepository enchantmentRepository;
    private final File enchantmentDirectory = new File(rootDirectory, "enchantments/");
    
    @Inject
    public EnchantmentDatabase(File rootDirectory, EnchantmentRepository enchantmentRepository) {
        super(rootDirectory);
        this.enchantmentRepository = enchantmentRepository;
    }

    @Override
    public void initialize() {
        try {
            if (!enchantmentDirectory.exists()) {
                enchantmentDirectory.mkdir();
            }
            File[] files = enchantmentDirectory.listFiles();
            if (files.length > 0) {
                // Use this data
                enchantmentRepository.getEnchantments().clear();
                for(File enchantmentFile : files) {
                    enchantmentRepository.getEnchantments().add(readObject(enchantmentFile, Enchantment.class));
                }
            } else {
                // Populate with defaults
                enchantmentRepository.getEnchantments().forEach(enchant -> {
                    File enchantmentFile = new File(enchantmentDirectory, enchant.getId().name() + ".json");
                    writeObject(enchantmentFile, enchant);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
