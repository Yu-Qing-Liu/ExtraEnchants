package com.github.yuqingliu.extraenchants.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.api.persistence.Database;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.enchantment.implementations.custom.*;
import com.github.yuqingliu.extraenchants.enchantment.implementations.AbilityEnchantment;
import com.github.yuqingliu.extraenchants.enchantment.implementations.ability.*;
import com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla.*;
import com.github.yuqingliu.extraenchants.persistence.enchantments.EnchantmentDTO;
import com.github.yuqingliu.extraenchants.persistence.enchantments.EnchantmentDatabase;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@Singleton
public class EnchantmentRepositoryImpl implements EnchantmentRepository {
    private EnchantmentDatabase enchantmentDatabase;
    private final Set<Enchantment> enchantments = new LinkedHashSet<>();
    private final ItemRepository itemRepository;
    private final ManagerRepository managerRepository;
    private final NamedTextColor common = Rarity.COMMON.color();
    private final NamedTextColor uncommon = Rarity.UNCOMMON.color();
    private final NamedTextColor rare = Rarity.RARE.color();
    private final NamedTextColor epic = Rarity.EPIC.color();
    private final NamedTextColor legendary = Rarity.LEGENDARY.color();
    private final NamedTextColor mythic = Rarity.MYTHIC.color();
    private final NamedTextColor descriptionColor = NamedTextColor.DARK_GRAY;
    
    @Inject
    public EnchantmentRepositoryImpl(ManagerRepository managerRepository, ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
        this.managerRepository = managerRepository;
    }
    
    @Override
    public void postConstruct() {
        // common enchants
        enchantments.add(new AutoLooting(managerRepository, this, itemRepository, common, descriptionColor));
        enchantments.add(new Delicate(managerRepository, this, itemRepository, common, descriptionColor));
        enchantments.add(new Replant(managerRepository, this, itemRepository, common, descriptionColor));
        enchantments.add(new Smelting(managerRepository, this, itemRepository, common, descriptionColor));
        // uncommon enchants
        enchantments.add(new AquaInfinity(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new BaneOfArthropods(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new BlastProtection(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new Channeling(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new DepthStrider(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new Efficiency(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new FeatherFalling(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new FireAspect(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new FireProtection(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new Flame(managerRepository, this, itemRepository, uncommon, descriptionColor));
        enchantments.add(new Fortune(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new FrostWalker(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new Impaling(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new Infinity(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new Knockback(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new Looting(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new Loyalty(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new LuckOfTheSea(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new Lure(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new Multishot(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new Piercing(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new Power(managerRepository, this, itemRepository, uncommon, descriptionColor));
        enchantments.add(new ProjectileProtection(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new Protection(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new Punch(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new QuickCharge(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new Respiration(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new Riptide(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new Sharpness(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new SilkTouch(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new Smite(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new SoulSpeed(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new SweepingEdge(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new SwiftSneak(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new Thorns(managerRepository, this, uncommon, descriptionColor));
        enchantments.add(new Unbreaking(managerRepository, this, uncommon, descriptionColor));
        // rare enchants
        enchantments.add(new Mending(managerRepository, this, rare, descriptionColor));
        enchantments.add(new VanishingCurse(managerRepository, this, rare, descriptionColor));
        enchantments.add(new BindingCurse(managerRepository, this, rare, descriptionColor));
        enchantments.add(new Growth(managerRepository, this, itemRepository, rare, descriptionColor));
        enchantments.add(new Venom(managerRepository, this, itemRepository, rare, descriptionColor));
        enchantments.add(new Wither(managerRepository, this, itemRepository, rare, descriptionColor));
        // epic encants
        enchantments.add(new LifeSteal(managerRepository, this, itemRepository, epic, descriptionColor));
        enchantments.add(new PowerStrike(managerRepository, this, itemRepository, epic, descriptionColor));
        enchantments.add(new Snipe(managerRepository, this, itemRepository, epic, descriptionColor));
        enchantments.add(new Warped(managerRepository, this, itemRepository, epic, descriptionColor));
        // legendary enchants
        enchantments.add(new Homing(managerRepository, this, itemRepository, legendary, descriptionColor));
        enchantments.add(new Immolate(managerRepository, this, itemRepository, legendary, descriptionColor));
        enchantments.add(new Mitigation(managerRepository, this, itemRepository, legendary, descriptionColor));
        enchantments.add(new Thunder(managerRepository, this, itemRepository, legendary, descriptionColor));
        // mythic enchants
        enchantments.add(new Focus(managerRepository, this, itemRepository, mythic, descriptionColor));
        enchantments.add(new RapidFire(managerRepository, this, itemRepository, mythic, descriptionColor));
        enchantments.add(new SonicBoom(managerRepository, this, itemRepository, mythic, descriptionColor));
        // Register enchantments
        enchantments.forEach(enchant -> enchant.postConstruct());
    }

    @Override
    public void setDatabase(Database database) {
        this.enchantmentDatabase = (EnchantmentDatabase) database;
    }
    
    @Override
    public Set<Enchantment> getEnchantments() {
        return enchantments.stream().filter(enchant -> enchant.getMaxLevel() > 0).collect(Collectors.toSet());
    }

    @Override
    public void setEnchantmentMaxLevel(EnchantID id, int maxLevel) {
        enchantments.forEach(enchant -> {
            if(enchant.getId() == id) {
                enchant.setMaxLevel(maxLevel);
                writeToDatabase(enchant);
            }
        });
    }

    @Override
    public void addApplicable(EnchantID id, Item item) {
        enchantments.forEach(enchant -> {
            if(enchant.getId() == id) {
                enchant.getApplicable().add(item);
                writeToDatabase(enchant);
            }
        });       
    }

    @Override
    public Enchantment getEnchantment(EnchantID id) {
        return getEnchantments().stream().filter(enchant -> enchant.getId() == id).findFirst().orElse(null);
    }

    @Override
    public Enchantment getEnchantment(Component enchantName) {
        return getEnchantments().stream().filter(enchant -> {
            for(int i = 1; i <= enchant.getMaxLevel(); i++) {
                if(enchant.getName(i).equals(enchantName)) {
                    return true;
                }
            }
            return false;
        }).findFirst().orElse(null);
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments(ItemStack item) {
        Map<Enchantment, Integer> enchants = new HashMap<>();
        getEnchantments().stream().forEach(enchant -> {
            if(enchant.getEnchantmentLevel(item) > 0) {
                enchants.put(enchant, enchant.getEnchantmentLevel(item));
            }
        });
        return enchants;
    }

    @Override
    public Map<Enchantment, Integer> getSortedEnchantments() {
        Map<Enchantment, Integer> enchants = new HashMap<>();
        getEnchantments().stream().forEach(enchant -> enchants.put(enchant, enchant.getMaxLevel()));
        List<Map.Entry<Enchantment, Integer>> sortedEntries = new ArrayList<>(enchants.entrySet());
        sortedEntries.sort(Comparator
            .comparingInt((Map.Entry<Enchantment, Integer> entry) -> entry.getKey().getId().rarity().rank())
            .thenComparing((Map.Entry<Enchantment, Integer> entry) -> entry.getValue())
        );
        Map<Enchantment, Integer> sortedEnchants = new LinkedHashMap<>();
        for (Map.Entry<Enchantment, Integer> entry : sortedEntries) {
            sortedEnchants.put(entry.getKey(), entry.getValue());
        }
        return sortedEnchants;
    }

    @Override
    public Map<Enchantment, Integer> getSortedEnchantments(ItemStack item) {
        Map<Enchantment, Integer> enchants = new HashMap<>();
        getEnchantments().stream()
            .filter(enchant -> enchant.getEnchantmentLevel(item) > 0 && !(enchant instanceof AbilityEnchantment))
            .forEach(enchant -> enchants.put(enchant, enchant.getEnchantmentLevel(item)));
        List<Map.Entry<Enchantment, Integer>> sortedEntries = new ArrayList<>(enchants.entrySet());
        sortedEntries.sort(Comparator
            .comparingInt((Map.Entry<Enchantment, Integer> entry) -> entry.getKey().getId().rarity().rank())
            .thenComparing((Map.Entry<Enchantment, Integer> entry) -> entry.getValue())
        );
        Map<Enchantment, Integer> sortedEnchants = new LinkedHashMap<>();
        for (Map.Entry<Enchantment, Integer> entry : sortedEntries) {
            sortedEnchants.put(entry.getKey(), entry.getValue());
        }
        return sortedEnchants;
    }

    @Override
    public Map<Enchantment, Integer> getSortedEnchantments(ItemStack item, Enchantment newEnchant, int newEnchantLevel) {
        Map<Enchantment, Integer> enchants = new HashMap<>();
        getEnchantments().stream()
            .filter(enchant -> enchant.getEnchantmentLevel(item) > 0 && !(enchant instanceof AbilityEnchantment))
            .forEach(enchant -> enchants.put(enchant, enchant.getEnchantmentLevel(item)));
        enchants.put(newEnchant, newEnchantLevel);
        List<Map.Entry<Enchantment, Integer>> sortedEntries = new ArrayList<>(enchants.entrySet());
        sortedEntries.sort(Comparator
            .comparingInt((Map.Entry<Enchantment, Integer> entry) -> entry.getKey().getId().rarity().rank())
            .thenComparing((Map.Entry<Enchantment, Integer> entry) -> entry.getValue())
        );
        Map<Enchantment, Integer> sortedEnchants = new LinkedHashMap<>();
        for (Map.Entry<Enchantment, Integer> entry : sortedEntries) {
            sortedEnchants.put(entry.getKey(), entry.getValue());
        }
        return sortedEnchants;
    }
    
    @Override
    public Enchantment[] getApplicableEnchantments(ItemStack item) {
        Map<Enchantment, Integer> itemEnchants = getEnchantments(item);
        return getEnchantments().stream().filter(enchant -> 
            enchant.canEnchant(item) && (itemEnchants.containsKey(enchant) ? (itemEnchants.get(enchant) < enchant.getMaxLevel()) : true)
        ).toArray(Enchantment[]::new);
    }

    @Override
    public Enchantment[] getApplicableEnchantmentsByRarity(ItemStack item, Rarity rarity) {
        Map<Enchantment, Integer> itemEnchants = getEnchantments(item);
        return getEnchantments().stream().filter(enchant -> 
            EnchantID.getEnchantmentIdsByRarity(rarity).contains(enchant.getId()) && enchant.canEnchant(item) && (itemEnchants.containsKey(enchant) ? (itemEnchants.get(enchant) < enchant.getMaxLevel()) : true)
        ).toArray(Enchantment[]::new);
    }

    private void writeToDatabase(Enchantment enchant) {
        enchantmentDatabase.writeAsyncObject(enchantmentDatabase.getEnchantmentFile(
            enchant.getId()), new EnchantmentDTO (
                enchant.getId(),
                enchant.getName(),
                enchant.getDescription(),
                enchant.getCooldown(),
                enchant.getMaxLevel(),
                enchant.getApplicable(),
                enchant.getConflicting(),
                enchant.getRequiredLevelFormula(),
                enchant.getCostFormula(),
                enchant.getLeveledColors()
            )
        );
    }
}
