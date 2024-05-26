package com.github.yuqingliu.extraenchants.api;

import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.function.Consumer;

/**
 * The internal scheduler for the plugin.
 */
public final class Scheduler {
    private Scheduler() {}

    @Setter
    private static JavaPlugin plugin;

    public static BukkitTask runLater(Consumer<Task> consumer, Duration duration) {
        Task task = new Task(plugin, consumer);
        task.runTaskLater(duration);
        return task.getTask();
    }

    public static BukkitTask runTimer(Consumer<Task> consumer, Duration period, Duration wait) {
        Task task = new Task(plugin, consumer);
        task.runTaskTimer(wait, period);
        return task.getTask();
    }

    public static BukkitTask runAsync(Consumer<Task> consumer) {
        Task task = new Task(plugin, consumer);
        task.runAsyncTask();
        return task.getTask();
    }

    public static BukkitTask runLaterAsync(Consumer<Task> consumer, Duration duration) {
        Task task = new Task(plugin, consumer);
        task.runAsyncTaskLater(duration);
        return task.getTask();
    }

    public static BukkitTask runTimerAsync(Consumer<Task> consumer, Duration period, Duration wait) {
        Task task = new Task(plugin, consumer);
        task.runAsyncTaskTimer(wait, period);
        return task.getTask();
    }
}

