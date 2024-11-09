package com.github.yuqingliu.extraenchants.persistence.enchantments;

import java.io.File;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
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
    
    @Inject
    public void postConstruct() {
        try {
            if (!enchantmentDirectory.exists()) {
                enchantmentDirectory.mkdir();
            }
            File[] files = enchantmentDirectory.listFiles();
            if (files.length > 0) {
                // Update data
                for (File enchantmentFile : files) {
                    EnchantmentDTO data = readObject(enchantmentFile, EnchantmentDTO.class);
                    EnchantID enchantID = data.getEnchantID();
                    mergeData(data, enchantmentRepository.getEnchantment(enchantID));
                }
            } else {
                // Populate with defaults
                enchantmentRepository.getEnchantments().forEach(enchant -> {
                    File enchantmentFile = new File(enchantmentDirectory, enchant.getId().name() + ".json");
                    EnchantmentDTO data = new EnchantmentDTO(enchant.getId(), enchant.getName(),
                            enchant.getDescription(), enchant.getCooldown(), enchant.getMaxLevel(), enchant.getApplicable(),
                            enchant.getConflicting(), enchant.getRequiredLevelFormula(), enchant.getCostFormula(),
                            enchant.getLeveledColors());
                    writeObject(enchantmentFile, data);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mergeData(EnchantmentDTO data, Enchantment enchantment) {
        enchantment.setName(data.getName());
        enchantment.setDescription(data.getDescription());
        enchantment.setCooldown(data.getCooldown());
        enchantment.setMaxLevel(data.getMaxLevel());
        enchantment.setApplicable(data.getApplicable());
        enchantment.setConflicting(data.getConflicting());
        enchantment.setRequiredLevelFormula(data.getRequiredLevelFormula());
        enchantment.setCostFormula(data.getCostFormula());
        enchantment.setLeveledColors(data.getLeveledColors());
    }
}
