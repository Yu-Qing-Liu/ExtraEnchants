package com.github.yuqingliu.extraenchants.enchantment;

import com.github.yuqingliu.extraenchants.api.enchantment.EnchantmentManager;
import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.item.ApplicableItemsRegistry;

import com.github.yuqingliu.extraenchants.enchantment.implementations.custom.*;
import com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla.*;
import com.github.yuqingliu.extraenchants.enchantment.implementations.ability.*;
import com.github.yuqingliu.extraenchants.ExtraEnchantsImpl;
import com.github.yuqingliu.extraenchants.enchantment.implementations.*;
import com.github.yuqingliu.extraenchants.api.utils.TextUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EnchantmentManagerImpl implements EnchantmentManager {
    protected JavaPlugin plugin;
    protected FileConfiguration config;
    protected ApplicableItemsRegistry applicable;
    private NamedTextColor vanilla = NamedTextColor.BLUE;
    private NamedTextColor custom = NamedTextColor.BLUE;
    private NamedTextColor ability = NamedTextColor.GOLD;
    private NamedTextColor descriptionColor = NamedTextColor.DARK_GRAY;
    private Map<String, Enchantment> enchantmentRegistry = new HashMap<>();

    public EnchantmentManagerImpl(ExtraEnchantsImpl plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.applicable = plugin.getApplicableItemsRegistry();
        initializeEnchantments();
        registerEnchants();
    }

    public Map<String, Enchantment> getEnchantments() {
        return this.enchantmentRegistry;
    }

    public Enchantment getEnchantment(String className) {
        return this.enchantmentRegistry.get(className);
    }

    private void registerEnchants() {
        insertEnchants();
        plugin.saveConfig();
        updateEnchants();
    }

    private void initializeEnchantments() {
        // VanillaEnchantments
        enchantmentRegistry.put(AquaInfinity.class.getSimpleName(), new Enchantment(new AquaInfinity(vanilla, descriptionColor)));
        enchantmentRegistry.put(BaneOfArthropods.class.getSimpleName(), new Enchantment(new BaneOfArthropods(vanilla, descriptionColor)));
        enchantmentRegistry.put(BindingCurse.class.getSimpleName(), new Enchantment(new BindingCurse(vanilla, descriptionColor)));
        enchantmentRegistry.put(BlastProtection.class.getSimpleName(), new Enchantment(new BlastProtection(vanilla, descriptionColor)));
        enchantmentRegistry.put(Channeling.class.getSimpleName(), new Enchantment(new Channeling(vanilla, descriptionColor)));
        enchantmentRegistry.put(DepthStrider.class.getSimpleName(), new Enchantment(new DepthStrider(vanilla, descriptionColor)));
        enchantmentRegistry.put(Efficiency.class.getSimpleName(), new Enchantment(new Efficiency(vanilla, descriptionColor)));
        enchantmentRegistry.put(FeatherFalling.class.getSimpleName(), new Enchantment(new FeatherFalling(vanilla, descriptionColor)));
        enchantmentRegistry.put(FireAspect.class.getSimpleName(), new Enchantment(new FireAspect(vanilla, descriptionColor)));
        enchantmentRegistry.put(FireProtection.class.getSimpleName(), new Enchantment(new FireProtection(vanilla, descriptionColor)));
        enchantmentRegistry.put(Flame.class.getSimpleName(), new Enchantment(new Flame(vanilla, descriptionColor)));
        enchantmentRegistry.put(Fortune.class.getSimpleName(), new Enchantment(new Fortune(vanilla, descriptionColor)));
        enchantmentRegistry.put(FrostWalker.class.getSimpleName(), new Enchantment(new FrostWalker(vanilla, descriptionColor)));
        enchantmentRegistry.put(Impaling.class.getSimpleName(), new Enchantment(new Impaling(vanilla, descriptionColor)));
        enchantmentRegistry.put(Infinity.class.getSimpleName(), new Enchantment(new Infinity(vanilla, descriptionColor)));
        enchantmentRegistry.put(Knockback.class.getSimpleName(), new Enchantment(new Knockback(vanilla, descriptionColor)));
        enchantmentRegistry.put(Looting.class.getSimpleName(), new Enchantment(new Looting(vanilla, descriptionColor)));
        enchantmentRegistry.put(Loyalty.class.getSimpleName(), new Enchantment(new Loyalty(vanilla, descriptionColor)));
        enchantmentRegistry.put(LuckOfTheSea.class.getSimpleName(), new Enchantment(new LuckOfTheSea(vanilla, descriptionColor)));
        enchantmentRegistry.put(Lure.class.getSimpleName(), new Enchantment(new Lure(vanilla, descriptionColor)));
        enchantmentRegistry.put(Mending.class.getSimpleName(), new Enchantment(new Mending(vanilla, descriptionColor)));
        enchantmentRegistry.put(Multishot.class.getSimpleName(), new Enchantment(new Multishot(vanilla, descriptionColor)));
        enchantmentRegistry.put(Piercing.class.getSimpleName(), new Enchantment(new Piercing(vanilla, descriptionColor)));
        enchantmentRegistry.put(Power.class.getSimpleName(), new Enchantment(new Power(vanilla, descriptionColor)));
        enchantmentRegistry.put(ProjectileProtection.class.getSimpleName(), new Enchantment(new ProjectileProtection(vanilla, descriptionColor)));
        enchantmentRegistry.put(Protection.class.getSimpleName(), new Enchantment(new Protection(vanilla, descriptionColor)));
        enchantmentRegistry.put(Punch.class.getSimpleName(), new Enchantment(new Punch(vanilla, descriptionColor)));
        enchantmentRegistry.put(QuickCharge.class.getSimpleName(), new Enchantment(new QuickCharge(vanilla, descriptionColor)));
        enchantmentRegistry.put(Respiration.class.getSimpleName(), new Enchantment(new Respiration(vanilla, descriptionColor)));
        enchantmentRegistry.put(Riptide.class.getSimpleName(), new Enchantment(new Riptide(vanilla, descriptionColor)));
        enchantmentRegistry.put(Sharpness.class.getSimpleName(), new Enchantment(new Sharpness(vanilla, descriptionColor)));
        enchantmentRegistry.put(SilkTouch.class.getSimpleName(), new Enchantment(new SilkTouch(vanilla, descriptionColor)));
        enchantmentRegistry.put(Smite.class.getSimpleName(), new Enchantment(new Smite(vanilla, descriptionColor)));
        enchantmentRegistry.put(SoulSpeed.class.getSimpleName(), new Enchantment(new SoulSpeed(vanilla, descriptionColor)));
        enchantmentRegistry.put(SweepingEdge.class.getSimpleName(), new Enchantment(new SweepingEdge(vanilla, descriptionColor)));
        enchantmentRegistry.put(SwiftSneak.class.getSimpleName(), new Enchantment(new SwiftSneak(vanilla, descriptionColor)));
        enchantmentRegistry.put(Thorns.class.getSimpleName(), new Enchantment(new Thorns(vanilla, descriptionColor)));
        enchantmentRegistry.put(Unbreaking.class.getSimpleName(), new Enchantment(new Unbreaking(vanilla, descriptionColor)));
        enchantmentRegistry.put(VanishingCurse.class.getSimpleName(), new Enchantment(new VanishingCurse(vanilla, descriptionColor)));
        // Custom enchants
        enchantmentRegistry.put(AutoLooting.class.getSimpleName(), new Enchantment(new AutoLooting(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Delicate.class.getSimpleName(), new Enchantment(new Delicate(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Growth.class.getSimpleName(), new Enchantment(new Growth(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Homing.class.getSimpleName(), new Enchantment(new Homing(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(LifeSteal.class.getSimpleName(), new Enchantment(new LifeSteal(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Mitigation.class.getSimpleName(), new Enchantment(new Mitigation(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(PowerStrike.class.getSimpleName(), new Enchantment(new PowerStrike(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Replant.class.getSimpleName(), new Enchantment(new Replant(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Smelting.class.getSimpleName(), new Enchantment(new Smelting(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Snipe.class.getSimpleName(), new Enchantment(new Snipe(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Venom.class.getSimpleName(), new Enchantment(new Venom(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Warped.class.getSimpleName(), new Enchantment(new Warped(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Wither.class.getSimpleName(), new Enchantment(new Wither(custom, descriptionColor, applicable)));
        enchantmentRegistry.put(Immolate.class.getSimpleName(), new Enchantment(new Immolate(custom, descriptionColor, applicable)));
        // Ability enchants
        enchantmentRegistry.put(SonicBoom.class.getSimpleName(), new Enchantment(new SonicBoom(ability, descriptionColor, applicable)));
        enchantmentRegistry.put(Focus.class.getSimpleName(), new Enchantment(new Focus(ability, descriptionColor, applicable)));
        enchantmentRegistry.put(RapidFire.class.getSimpleName(), new Enchantment(new RapidFire(ability, descriptionColor, applicable)));
    }

    private void insertEnchants() {
        for(Map.Entry<String, Enchantment> entry : enchantmentRegistry.entrySet()) {
            String key = entry.getKey();
            Enchantment enchantment = entry.getValue();
            List<Material> applicableItems = enchantment.getApplicableItems();
            List<Component> applicableNames = enchantment.getApplicableDisplayNames();
            List<TextColor> leveledColors = enchantment.getLeveledColors();
            List<String> applicableMaterials = new ArrayList<>();
            List<String> applicableDisplayNames = new ArrayList<>();
            List<String> levelColors = new ArrayList<>();
            for(Material material : applicableItems) {
                applicableMaterials.add(material.name());
            }
            for(Component name : applicableNames) {
                String data = TextUtils.componentToJson(name);
                applicableDisplayNames.add(data);
            }
            for(TextColor color : leveledColors) {
                levelColors.add(color.asHexString());
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
                    applicableDisplayNames,
                    levelColors
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
                updateEnchantment(value, key);
            }
        }
    }

    private void updateEnchantment(Object data, String key) {
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
            List<?> leveledColors = (List<?>) info.get(7);
            List<Material> applicableItems = parseMaterialList(applicableMaterials);
            List<Component> applicableNames = parseNamesList(applicableDisplayNames);
            List<TextColor> levelColors = parseLevelColors(leveledColors);
            enchantmentRegistry.get(key).setName(name);
            enchantmentRegistry.get(key).setMaxLevel(maxLevel);
            enchantmentRegistry.get(key).setDescription(description);
            enchantmentRegistry.get(key).setApplicable(applicableItems);
            enchantmentRegistry.get(key).setApplicableDisplayNames(applicableNames);
            enchantmentRegistry.get(key).setRequiredLevelFormula(levelFormula);
            enchantmentRegistry.get(key).setCostFormula(costFormula);
            enchantmentRegistry.get(key).setLeveledColors(levelColors);
        }
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

    private List<TextColor> parseLevelColors(List<?> data) {
        List<TextColor> colors = new ArrayList<>();
        for(Object T : data) {
            String t = (String) T;
            TextColor color = TextColor.fromHexString(t);
            colors.add(color);
        }
        return colors;
    }
}
