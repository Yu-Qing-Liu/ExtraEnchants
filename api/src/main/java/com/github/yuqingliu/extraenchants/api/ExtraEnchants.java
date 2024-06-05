package com.github.yuqingliu.extraenchants.api;

import java.util.Objects;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import com.github.yuqingliu.extraenchants.api.command.CommandManager;
import com.github.yuqingliu.extraenchants.api.enchantment.EnchantmentManager;
import com.github.yuqingliu.extraenchants.api.item.ApplicableItemsRegistry;

public abstract class ExtraEnchants extends JavaPlugin {
    private static ExtraEnchants instance;

    @Override
    public void onLoad() {
        if(instance != null) {
            throw new RuntimeException();
        }
        instance = this;
        Scheduler.setPlugin(instance);
        Keys.load(instance);
    }

    /**
     * Gets a main instance of ExtraEnchants.
     * @return ExtraEnchants
     */
    public static @NotNull ExtraEnchants getInstance() {
        return Objects.requireNonNull(instance);
    }

    public abstract @NotNull ApplicableItemsRegistry getApplicableItemsRegistry();

    public abstract @NotNull EnchantmentManager getEnchantmentManager();

    public abstract @NotNull CommandManager getCommandManager();
}
