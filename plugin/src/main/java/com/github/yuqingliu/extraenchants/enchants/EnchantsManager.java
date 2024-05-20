package com.github.yuqingliu.extraenchants.enchants;

import com.github.yuqingliu.extraenchants.enchants.bow.*;
import com.github.yuqingliu.extraenchants.enchants.crossbow.*;
import com.github.yuqingliu.extraenchants.enchants.armor.*;
import com.github.yuqingliu.extraenchants.enchants.melee.*;
import com.github.yuqingliu.extraenchants.enchants.tools.*;
import com.github.yuqingliu.extraenchants.enchants.ranged.*;
import com.github.yuqingliu.extraenchants.enchants.weapons.*;
import com.github.yuqingliu.extraenchants.enchants.universal.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.event.Listener;

import com.github.yuqingliu.extraenchants.ExtraEnchants;
import com.github.yuqingliu.extraenchants.configuration.implementations.CooldownConstants;
import com.github.yuqingliu.extraenchants.enchantment.Enchantment;

public class EnchantsManager {
    private ExtraEnchants plugin;
    private Map<String, Enchantment> registry;
    private Map<String, Integer> cooldownRegistry;
    private List<Listener> listeners = new ArrayList<>();

    public EnchantsManager(ExtraEnchants plugin) {
        this.plugin = plugin;
        this.registry = plugin.getEnchantmentManager().getEnchantments();
        CooldownConstants cooldowns = (CooldownConstants) plugin.getConfigurationManager().getConstants().get("CooldownConstants");
        this.cooldownRegistry = cooldowns.getCooldownRegistry();
        listeners.add(new Homing(registry.get("Homing")));
        listeners.add(new Mitigation(registry.get("Mitigation")));
        listeners.add(new Growth(registry.get("Growth")));
        listeners.add(new Snipe(registry.get("Snipe")));
        listeners.add(new Flame(registry.get("Flame")));
        listeners.add(new Power(registry.get("Power")));
        listeners.add(new Wither(plugin, registry.get("Wither")));
        listeners.add(new Venom(plugin, registry.get("Venom")));
        listeners.add(new SonicBoom(registry.get("SonicBoom"), cooldownRegistry.get("SonicBoom")));
        listeners.add(new Replant(registry.get("Replant"), registry.get("AutoLooting")));
        listeners.add(new Delicate(registry.get("Delicate")));
        listeners.add(new Smelting(registry.get("Smelting"), registry.get("AutoLooting")));
        listeners.add(new AutoLooting(registry.get("AutoLooting"), registry.get("Smelting"), registry.get("Replant")));
        listeners.add(new SilkTouch(registry.get("SilkTouch"), registry.get("AutoLooting"), registry.get("Smelting"), registry.get("Replant")));
        listeners.add(new PowerStrike(registry.get("PowerStrike")));
        listeners.add(new Focus(plugin, registry.get("Focus")));
        listeners.add(new Warped(registry.get("Warped")));
        listeners.add(new LifeSteal(registry.get("LifeSteal")));
        listeners.add(new RapidFire(registry.get("RapidFire"), cooldownRegistry.get("RapidFire")));
    }

    public void registerListeners() {
        for(Listener listener : listeners) {
            String listenerName = listener.getClass().getSimpleName();
            Enchantment correspondingEnchantment = registry.get(listenerName);
            if(correspondingEnchantment.getMaxLevel() > 0) {
                plugin.getPluginManager().registerEvents(listener, plugin);
            }
        }
    }
}
