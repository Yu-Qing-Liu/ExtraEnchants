package com.github.yuqingliu.extraenchants.lore.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.managers.TextManager;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.Rarity;
import com.github.yuqingliu.extraenchants.lore.AbstractLoreSection;
import com.google.inject.Inject;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class EnchantmentSection extends AbstractLoreSection {
    private final EnchantmentRepository enchantmentRepository;
    private final Component comma = Component.text(", ", NamedTextColor.BLUE);
    private final int maxComponents = 3;

    @Inject
    public EnchantmentSection(int position, TextManager textManager, int[] sectionSizes, List<Component> itemLore,
            EnchantmentRepository enchantmentRepository) {
        super(position, textManager, sectionSizes, itemLore);
        this.enchantmentRepository = enchantmentRepository;
        name = this.getClass().getSimpleName();
    }

    @Getter
    private class Enchant {
        private final Component name;
        private final Component eLevel;
        private final int level;
        private final Rarity rarity;

        public Enchant(Component name, Component eLevel) {
            this.name = name;
            this.eLevel = eLevel;
            this.level = textManager.fromRoman(textManager.componentToString(eLevel));
            this.rarity = enchantmentRepository.getEnchantment(name).getId().rarity();
        }
    }

    public void addOrUpdateEnchantmentFromSection(Component enchant, Component eLevel) {
        List<Component> newLore = new ArrayList<>();
        List<Enchant> enchants = new ArrayList<>();

        // Flatten and filter the lore components in one go, removing all commas
        for (Component line : lore) {
            List<Component> subcomponents = line.children();
            List<Component> children = new ArrayList<>();
            children.addAll(subcomponents);
            children.add(Component.empty());
            children.add(Component.empty());
            children.add(Component.empty());
            for (int i = 0; i < children.size(); i++) {
                Component child = children.get(i);
                if (textManager.componentStringEquals(enchant, child)) {
                    i++;
                } else if (!child.equals(comma) && !child.equals(Component.empty())) {
                    if(enchantmentRepository.getEnchantment(child) != null) {
                        enchants.add(new Enchant(child, children.get(i + 1)));
                    }
                }
            }
        }
        enchants.add(new Enchant(enchant, eLevel));

        List<Component> sortedComponents = sortEnchantments(enchants);
        sortedComponents.add(Component.empty());
        sortedComponents.add(Component.empty());
        sortedComponents.add(Component.empty());

        // Reconstruct the final lore from filtered components, ensuring max 8
        // components per line
        Component line = Component.empty();
        int componentCount = 0;
        for (int i = 0; i < sortedComponents.size(); i += 2) {
            Component component = sortedComponents.get(i);
            Component level = sortedComponents.get(i + 1);
            if (component.equals(Component.empty()) || level.equals(Component.empty())) {
                break;
            }

            if (componentCount < maxComponents) {
                if (componentCount != 0) {
                    line = line.append(comma);
                }
                line = line.append(component);
                line = line.append(level);
                componentCount++;
            } else {
                newLore.add(line);
                line = Component.empty().append(component);
                line = line.append(level);
                componentCount = 1;
            }
        }
        if (!line.equals(Component.empty())) {
            newLore.add(line);
        }
        this.lore = newLore;
    }

    public void removeEnchantmentFromSection(Component enchant) {
        List<Component> newLore = new ArrayList<>();
        List<Component> filteredComponents = new ArrayList<>();

        // Flatten and filter the lore components in one go, removing all commas
        for (Component line : lore) {
            List<Component> children = line.children();
            for (int i = 0; i < children.size(); i++) {
                Component child = children.get(i);
                if (textManager.componentStringEquals(enchant, child)) {
                    i++;
                } else if (!child.equals(comma) && !child.equals(Component.empty())) {
                    filteredComponents.add(child);
                }
            }
        }

        // Buffer
        filteredComponents.add(Component.empty());
        filteredComponents.add(Component.empty());
        filteredComponents.add(Component.empty());

        // Reconstruct the final lore from filtered components, ensuring max 8
        // components per line
        Component line = Component.empty();
        int componentCount = 0;
        for (int i = 0; i < filteredComponents.size(); i += 2) {
            Component component = filteredComponents.get(i);
            Component level = filteredComponents.get(i + 1);
            if (component.equals(Component.empty()) || level.equals(Component.empty())) {
                break;
            }

            if (componentCount < maxComponents) {
                if (componentCount != 0) {
                    line = line.append(comma);
                }
                line = line.append(component);
                line = line.append(level);
                componentCount++;
            } else {
                newLore.add(line);
                line = Component.empty().append(component);
                line = line.append(level);
                componentCount = 1;
            }
        }
        if (!line.equals(Component.empty())) {
            newLore.add(line);
        }
        this.lore = newLore;
    }

    private List<Component> sortEnchantments(List<Enchant> enchants) {
        List<Enchant> common = sortEnchantmentsByRarity(enchants, Rarity.COMMON);
        List<Enchant> uncommon = sortEnchantmentsByRarity(enchants, Rarity.UNCOMMON);
        List<Enchant> rare = sortEnchantmentsByRarity(enchants, Rarity.RARE);
        List<Enchant> epic = sortEnchantmentsByRarity(enchants, Rarity.EPIC);
        List<Enchant> legendary = sortEnchantmentsByRarity(enchants, Rarity.LEGENDARY);
        List<Enchant> mythic = sortEnchantmentsByRarity(enchants, Rarity.MYTHIC);
        List<Enchant> sorted = new ArrayList<>();
        sorted.addAll(common);
        sorted.addAll(uncommon);
        sorted.addAll(rare);
        sorted.addAll(epic);
        sorted.addAll(legendary);
        sorted.addAll(mythic);
        List<Component> sortedComponents = new ArrayList<>();
        for (Enchant e : sorted) {
            sortedComponents.add(e.getName());
            sortedComponents.add(e.getELevel());
        }
        return sortedComponents;
    }

    private List<Enchant> sortEnchantmentsByRarity(List<Enchant> enchants, Rarity rarity) {
        List<Enchant> sorted = new ArrayList<>();
        // Filter by common
        for (Enchant e : enchants) {
            if (e.getRarity() == rarity) {
                sorted.add(e);
            }
        }
        Collections.sort(sorted, Comparator.comparingInt(Enchant::getLevel));
        return sorted;
    }
}
