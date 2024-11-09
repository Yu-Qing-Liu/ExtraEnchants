package com.github.yuqingliu.extraenchants.repositories;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.inventory.ItemStack;
import org.eclipse.sisu.PostConstruct;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.enchantment.implementations.custom.*;
import com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla.*;
import com.github.yuqingliu.extraenchants.enchantment.implementations.ability.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;

@Getter
@Singleton
public class EnchantmentRepositoryImpl implements EnchantmentRepository {
    private final Set<Enchantment> enchantments = new LinkedHashSet<>();
    private final ItemRepository itemRepository;
    private final ManagerRepository managerRepository;
    private final NamedTextColor vanilla = NamedTextColor.BLUE;
    private final NamedTextColor custom = NamedTextColor.BLUE;
    private final NamedTextColor ability = NamedTextColor.GOLD;
    private final NamedTextColor descriptionColor = NamedTextColor.DARK_GRAY;
    
    @Inject
    public EnchantmentRepositoryImpl(ManagerRepository managerRepository, ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
        this.managerRepository = managerRepository;
    }
    
    @Override
    public void postConstruct() {
        // Vanilla enchants
        enchantments.add(new AquaInfinity(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new BaneOfArthropods(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new BindingCurse(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new BlastProtection(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new Channeling(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new DepthStrider(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new Efficiency(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new FeatherFalling(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new FireAspect(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new FireProtection(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new Flame(managerRepository, this, itemRepository, vanilla, descriptionColor));
        enchantments.add(new Fortune(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new FrostWalker(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new Impaling(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new Infinity(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new Knockback(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new Looting(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new Loyalty(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new LuckOfTheSea(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new Lure(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new Mending(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new Multishot(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new Piercing(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new Power(managerRepository, this, itemRepository, vanilla, descriptionColor));
        enchantments.add(new ProjectileProtection(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new Protection(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new Punch(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new QuickCharge(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new Respiration(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new Riptide(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new Sharpness(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new SilkTouch(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new Smite(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new SoulSpeed(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new SweepingEdge(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new SwiftSneak(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new Thorns(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new Unbreaking(managerRepository, this, vanilla, descriptionColor));
        enchantments.add(new VanishingCurse(managerRepository, this, vanilla, descriptionColor));
        // Custom enchants
        enchantments.add(new AutoLooting(managerRepository, this, itemRepository, custom, descriptionColor));
        enchantments.add(new Delicate(managerRepository, this, itemRepository, custom, descriptionColor));
        enchantments.add(new Growth(managerRepository, this, itemRepository, custom, descriptionColor));
        enchantments.add(new Homing(managerRepository, this, itemRepository, custom, descriptionColor));
        enchantments.add(new Immolate(managerRepository, this, itemRepository, custom, descriptionColor));
        enchantments.add(new LifeSteal(managerRepository, this, itemRepository, custom, descriptionColor));
        enchantments.add(new Mitigation(managerRepository, this, itemRepository, custom, descriptionColor));
        enchantments.add(new PowerStrike(managerRepository, this, itemRepository, custom, descriptionColor));
        enchantments.add(new Replant(managerRepository, this, itemRepository, custom, descriptionColor));
        enchantments.add(new Smelting(managerRepository, this, itemRepository, custom, descriptionColor));
        enchantments.add(new Snipe(managerRepository, this, itemRepository, custom, descriptionColor));
        enchantments.add(new Venom(managerRepository, this, itemRepository, custom, descriptionColor));
        enchantments.add(new Warped(managerRepository, this, itemRepository, custom, descriptionColor));
        enchantments.add(new Wither(managerRepository, this, itemRepository, custom, descriptionColor));
        // Ability enchants
        enchantments.add(new Focus(managerRepository, this, itemRepository, ability, descriptionColor));
        enchantments.add(new RapidFire(managerRepository, this, itemRepository, ability, descriptionColor));
        enchantments.add(new SonicBoom(managerRepository, this, itemRepository, ability, descriptionColor));

        // Register enchantments
        enchantments.forEach(enchant -> enchant.postConstruct());
    }
    
    @Override
    public Set<Enchantment> getEnchantments(Set<EnchantID> ids) {
        return enchantments.stream().filter(enchant -> ids.contains(enchant.getId())).collect(Collectors.toSet());
    }
    
    @Override
    public Enchantment getEnchantment(EnchantID id) {
        return enchantments.stream().filter(enchant -> enchant.getId() == id).findFirst().orElse(null);
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
    public Enchantment[] getApplicableEnchantments(ItemStack item) {
        Map<Enchantment, Integer> itemEnchants = getEnchantments(item);
        return enchantments.stream().filter(enchant -> 
            enchant.canEnchant(item) && (itemEnchants.containsKey(enchant) ? (itemEnchants.get(enchant) < enchant.getMaxLevel()) : true)
        ).toArray(Enchantment[]::new);
    }
}
