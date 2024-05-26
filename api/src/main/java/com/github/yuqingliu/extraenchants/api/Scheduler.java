package com.github.yuqingliu.extraenchants.api;

import lombok.AccessLevel;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.function.Consumer;

/**
 * The internal scheduler for the plugin.
 */
public final class Scheduler {
    private Scheduler() {}

    @Setter(AccessLevel.PACKAGE)
    private static JavaPlugin plugin;

    // 1s == 1000 ms == 20 ticks
    //    -> 1 tick == 50 ms
    private final static double MS_PER_TICKS = 50;
    public static long toTicks(Duration duration) {
        return (long) (duration.toMillis() / MS_PER_TICKS);
    }

    public static BukkitTask runAsync(Consumer<BukkitTask> runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> runnable.accept(null));
    }

    public static BukkitTask runLater(Consumer<BukkitTask> runnable, Duration duration) {
        return Bukkit.getScheduler().runTaskLater(plugin, () -> runnable.accept(null), toTicks(duration));
    }

    public static BukkitTask runLaterAsync(Consumer<BukkitTask> runnable, Duration duration) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> runnable.accept(null), toTicks(duration));
    }

    public static BukkitTask runTimer(Consumer<BukkitTask> runnable, Duration period, Duration wait) {
        return Bukkit.getScheduler().runTaskTimer(plugin, () -> runnable.accept(null), toTicks(wait), toTicks(period));
    }

    public static BukkitTask runTimerAsync(Consumer<BukkitTask> runnable, Duration period, Duration wait) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> runnable.accept(null), toTicks(wait), toTicks(period));
    }
}
