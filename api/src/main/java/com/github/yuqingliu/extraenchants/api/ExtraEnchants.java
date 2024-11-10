package com.github.yuqingliu.extraenchants.api;

import java.util.Objects;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ExtraEnchants extends JavaPlugin {
    private static ExtraEnchants instance;

    @Override
    public void onLoad() {
        if (instance != null) {
            throw new RuntimeException("Multiple instances of ExtraEnchants detected!");
        }
        instance = this;
        Scheduler.setPlugin(instance);
    }

    /**
     * Gets a main instance of ExtraEnchants.
     * @return ExtraEnchants
     */
    public static ExtraEnchants getInstance() {
        return Objects.requireNonNull(instance);
    }
}

