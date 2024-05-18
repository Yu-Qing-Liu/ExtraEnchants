package com.github.yuqingliu.extraenchants.enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.custom.*;
import com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla.*;
import com.github.yuqingliu.extraenchants.item.ApplicableItemsRegistry;
import com.github.yuqingliu.extraenchants.ExtraEnchants;
import com.github.yuqingliu.extraenchants.enchantment.implementations.*;
import com.github.yuqingliu.extraenchants.utils.TextUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EnchantmentManager {
    protected JavaPlugin plugin;
    protected FileConfiguration config;
    protected ApplicableItemsRegistry applicable;
    private NamedTextColor vanilla = NamedTextColor.AQUA;
    private NamedTextColor custom = NamedTextColor.BLUE;
    private NamedTextColor descriptionColor = NamedTextColor.GRAY;
    private final Map<String, Enchantment> enchantmentRegistry = new HashMap<>();

    public EnchantmentManager(ExtraEnchants plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.applicable = plugin.getApplicableItemsRegistry();
        // VanillaEnchantments
        enchantmentRegistry.put(AquaInfinity.class.getSimpleName(), new Enchantment(new AquaInfinity(vanilla)));
        enchantmentRegistry.put(BaneOfArthropods.class.getSimpleName(), new Enchantment(new BaneOfArthropods(vanilla)));
        enchantmentRegistry.put(BindingCurse.class.getSimpleName(), new Enchantment(new BindingCurse(vanilla)));
        enchantmentRegistry.put(BlastProtection.class.getSimpleName(), new Enchantment(new BlastProtection(vanilla)));
        enchantmentRegistry.put(Channeling.class.getSimpleName(), new Enchantment(new Channeling(vanilla)));
        enchantmentRegistry.put(DepthStrider.class.getSimpleName(), new Enchantment(new DepthStrider(vanilla)));
        enchantmentRegistry.put(Efficiency.class.getSimpleName(), new Enchantment(new Efficiency(vanilla)));
        enchantmentRegistry.put(FeatherFalling.class.getSimpleName(), new Enchantment(new FeatherFalling(vanilla)));
        enchantmentRegistry.put(FireAspect.class.getSimpleName(), new Enchantment(new FireAspect(vanilla)));
        enchantmentRegistry.put(FireProtection.class.getSimpleName(), new Enchantment(new FireProtection(vanilla)));
        enchantmentRegistry.put(Flame.class.getSimpleName(), new Enchantment(new Flame(vanilla)));
        enchantmentRegistry.put(Fortune.class.getSimpleName(), new Enchantment(new Fortune(vanilla)));
        enchantmentRegistry.put(FrostWalker.class.getSimpleName(), new Enchantment(new FrostWalker(vanilla)));
        enchantmentRegistry.put(Impaling.class.getSimpleName(), new Enchantment(new Impaling(vanilla)));
        enchantmentRegistry.put(Infinity.class.getSimpleName(), new Enchantment(new Infinity(vanilla)));
        enchantmentRegistry.put(Knockback.class.getSimpleName(), new Enchantment(new Knockback(vanilla)));
        enchantmentRegistry.put(Looting.class.getSimpleName(), new Enchantment(new Looting(vanilla)));
        enchantmentRegistry.put(Loyalty.class.getSimpleName(), new Enchantment(new Loyalty(vanilla)));
        enchantmentRegistry.put(LuckOfTheSea.class.getSimpleName(), new Enchantment(new LuckOfTheSea(vanilla)));
        enchantmentRegistry.put(Lure.class.getSimpleName(), new Enchantment(new Lure(vanilla)));
        enchantmentRegistry.put(Mending.class.getSimpleName(), new Enchantment(new Mending(vanilla)));
        enchantmentRegistry.put(Multishot.class.getSimpleName(), new Enchantment(new Multishot(vanilla)));
        enchantmentRegistry.put(Piercing.class.getSimpleName(), new Enchantment(new Piercing(vanilla)));
        enchantmentRegistry.put(Power.class.getSimpleName(), new Enchantment(new Power(vanilla)));
        enchantmentRegistry.put(ProjectileProtection.class.getSimpleName(), new Enchantment(new ProjectileProtection(vanilla)));
        enchantmentRegistry.put(Protection.class.getSimpleName(), new Enchantment(new Protection(vanilla)));
        enchantmentRegistry.put(Punch.class.getSimpleName(), new Enchantment(new Punch(vanilla)));
        enchantmentRegistry.put(QuickCharge.class.getSimpleName(), new Enchantment(new QuickCharge(vanilla)));
        enchantmentRegistry.put(Respiration.class.getSimpleName(), new Enchantment(new Respiration(vanilla)));
        enchantmentRegistry.put(Riptide.class.getSimpleName(), new Enchantment(new Riptide(vanilla)));
        enchantmentRegistry.put(Sharpness.class.getSimpleName(), new Enchantment(new Sharpness(vanilla)));
        enchantmentRegistry.put(SilkTouch.class.getSimpleName(), new Enchantment(new SilkTouch(vanilla)));
        enchantmentRegistry.put(Smite.class.getSimpleName(), new Enchantment(new Smite(vanilla)));
        enchantmentRegistry.put(SoulSpeed.class.getSimpleName(), new Enchantment(new SoulSpeed(vanilla)));
        enchantmentRegistry.put(SweepingEdge.class.getSimpleName(), new Enchantment(new SweepingEdge(vanilla)));
        enchantmentRegistry.put(SwiftSneak.class.getSimpleName(), new Enchantment(new SwiftSneak(vanilla)));
        enchantmentRegistry.put(Thorns.class.getSimpleName(), new Enchantment(new Thorns(vanilla)));
        enchantmentRegistry.put(Unbreaking.class.getSimpleName(), new Enchantment(new Unbreaking(vanilla)));
        enchantmentRegistry.put(VanishingCurse.class.getSimpleName(), new Enchantment(new VanishingCurse(vanilla)));
        // Custom enchants
        enchantmentRegistry.put(AutoLooting.class.getSimpleName(), new Enchantment(new AutoLooting(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Delicate.class.getSimpleName(), new Enchantment(new Delicate(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Focus.class.getSimpleName(), new Enchantment(new Focus(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Growth.class.getSimpleName(), new Enchantment(new Growth(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Homing.class.getSimpleName(), new Enchantment(new Homing(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(LifeSteal.class.getSimpleName(), new Enchantment(new LifeSteal(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Mitigation.class.getSimpleName(), new Enchantment(new Mitigation(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(PowerStrike.class.getSimpleName(), new Enchantment(new PowerStrike(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(RapidFire.class.getSimpleName(), new Enchantment(new RapidFire(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Replant.class.getSimpleName(), new Enchantment(new Replant(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Smelting.class.getSimpleName(), new Enchantment(new Smelting(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Snipe.class.getSimpleName(), new Enchantment(new Snipe(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(SonicBoom.class.getSimpleName(), new Enchantment(new SonicBoom(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Venom.class.getSimpleName(), new Enchantment(new Venom(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Warped.class.getSimpleName(), new Enchantment(new Warped(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Wither.class.getSimpleName(), new Enchantment(new Wither(custom, descriptionColor, applicable)));
    }

    public Map<String, Enchantment> getEnchantments() {
        return this.enchantmentRegistry;
    }

    public Enchantment getEnchantment(String className) {
        return this.enchantmentRegistry.get(className);
    }

    public void registerEnchants() {
        insertEnchants();
        plugin.saveConfig();
        updateEnchants();
    }

    private void insertEnchants() {
        for(Map.Entry<String, Enchantment> entry : enchantmentRegistry.entrySet()) {
            String key = entry.getKey();
            Enchantment enchantment = entry.getValue();
            List<Material> applicableItems = enchantment.getApplicableItems();
            List<Component> applicableNames = enchantment.getApplicableDisplayNames();
            List<String> applicableMaterials = new ArrayList<>();
            List<String> applicableDisplayNames = new ArrayList<>();
            for(Material material : applicableItems) {
                applicableMaterials.add(material.name());
            }
            for(Component name : applicableNames) {
                String data = TextUtils.componentToJson(name);
                applicableDisplayNames.add(data);
            }
            String path = "Enchantments." + key;
            if(!config.isSet(path)) {
                List<Object> defaultOptions = Arrays.asList(
                    TextUtils.componentToJson(enchantment.getName()),
                    enchantment.getMaxLevel(),
                    enchantment.getRequiredLevelFormula(),
                    enchantment.getCostFormula(),
                    TextUtils.componentToJson(enchantment.getDescription()),
                    applicableMaterials,
                    applicableDisplayNames
                );
                config.set(path, defaultOptions);
            }
        }
    }

    private void updateEnchants() {
        ConfigurationSection enchantmentsSection = config.getConfigurationSection("Enchantments");
        if(enchantmentsSection != null) {
            for(String key : enchantmentsSection.getKeys(false)) {
                Object value = enchantmentsSection.get(key);
                Enchantment enchantment = createEnchantment(value, key);
                if(enchantment != null) {
                    enchantmentRegistry.put(key, enchantment);
                }
            }
        }
    }

    private Enchantment createEnchantment(Object data, String key) {
        if(data instanceof List) {
            List<?> info = (List<?>) data;
            String sname = (String) info.get(0);
            Component name = TextUtils.jsonToComponent(sname);
            int maxLevel = (int) info.get(1);
            String levelFormula = (String) info.get(2);
            String costFormula = (String) info.get(3);
            String sdescription = (String) info.get(4);
            Component description = TextUtils.jsonToComponent(sdescription);
            List<?> applicableMaterials = (List<?>) info.get(5);
            List<?> applicableDisplayNames = (List<?>) info.get(6);
            List<Material> applicableItems = parseMaterialList(applicableMaterials);
            List<Component> applicableNames = parseNamesList(applicableDisplayNames);
            org.bukkit.enchantments.Enchantment enchantment = TextUtils.getVanillaEnchantment(key);
            if(enchantment != null) {
                return new Enchantment(
                    new VanillaEnchantment(
                        enchantment,
                        name,
                        maxLevel,
                        description,
                        applicableItems,
                        applicableNames,
                        levelFormula,
                        costFormula
                    )
                );
            }
            return new Enchantment(
                new CustomEnchantment(
                    name,
                    maxLevel,
                    description,
                    applicableItems,
                    applicableNames,
                    levelFormula,
                    costFormula
                )
            );
        }
        return null;
    }

    private List<Material> parseMaterialList(List<?> data) {
        List<Material> applicable = new ArrayList<>();
        for(Object T : data) {
            String t = (String) T;
            Material material = Material.matchMaterial(t);
            applicable.add(material);
        }
        return applicable;
    }

    private List<Component> parseNamesList(List<?> data) {
        List<Component> applicable = new ArrayList<>();
        for(Object T : data) {
            String t = (String) T;
            Component displayName = TextUtils.jsonToComponent(t);
            applicable.add(displayName);
        }
        return applicable;
    }
}
