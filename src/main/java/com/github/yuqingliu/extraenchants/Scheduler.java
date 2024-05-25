package com.github.yuqingliu.extraenchants;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;

/**
 * The internal scheduler for the plugin.
 * <br/>
 * Is set up on load by the main.
 */
public final class Scheduler {
    private Scheduler() {}

    @Setter
    private static JavaPlugin plugin;

    // 1s == 1000 ms == 20 ticks
    //    -> 1 tick == 50 ms
    private final static double MS_PER_TICKS = 50;
    public static long toTicks(Duration duration) {
        return (long) (duration.toMillis() / MS_PER_TICKS);
    }

    public static BukkitTask runAsync(Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public static BukkitTask runLater(Runnable runnable, Duration duration) {
        return Bukkit.getScheduler().runTaskLater(plugin, runnable, toTicks(duration));
    }

    public static BukkitTask runLaterAsync(Runnable runnable, Duration duration) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, toTicks(duration));
    }

    public static BukkitTask runTimer(Runnable runnable, Duration period, Duration wait) {
        return Bukkit.getScheduler().runTaskTimer(plugin, runnable, toTicks(wait), toTicks(period));
    }

    public static BukkitTask runTimerAsync(Runnable runnable, Duration period, Duration wait) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, toTicks(wait), toTicks(period));
    }
}

