package com.github.yuqingliu.extraenchants.api;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

/**
 * A basic registry to expose {@link NamespacedKey}.
 */
public final class Keys {
    private static Plugin plugin;
    private Keys() {}
    
    @Getter private static NamespacedKey itemLore;
    @Getter private static NamespacedKey customUIBlock;

    /**
     * Used by the Main on load.
     * @param plugin the namespace to use for the namespaced-keys.
     */
    public static void load(Plugin plugin) {
        Keys.plugin = plugin;
        itemLore = new NamespacedKey(plugin, "item-lore");
        customUIBlock = new NamespacedKey(plugin, "custom-ui-block");
    }

    public static NamespacedKey itemEnchant(String name) {
        String key = name.replace(" ", "_").toLowerCase();
        return new NamespacedKey(plugin, "item-enchant." + key);
    }
}

