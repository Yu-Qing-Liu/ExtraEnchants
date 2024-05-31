package com.github.yuqingliu.extraenchants.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;

import java.time.Duration;
import java.util.function.Consumer;

public class Task {
    private final static double MS_PER_TICKS = 50;
    private final Plugin plugin;
    private final Consumer<Task> taskConsumer;
    @Getter private BukkitTask task;

    public Task(Plugin plugin, Consumer<Task> taskConsumer) {
        this.plugin = plugin;
        this.taskConsumer = taskConsumer;
    }

    public void runTask() {
        this.task = Bukkit.getScheduler().runTask(plugin, () -> taskConsumer.accept(this));
    }

    public void runTaskLater(Duration delay) {
        this.task = Bukkit.getScheduler().runTaskLater(plugin, () -> taskConsumer.accept(this), toTicks(delay));
    }

    public void runTaskTimer(Duration delay, Duration interval) {
        this.task = Bukkit.getScheduler().runTaskTimer(plugin, () -> taskConsumer.accept(this), toTicks(delay), toTicks(interval));
    }

    public void runAsyncTask() {
        this.task = Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> taskConsumer.accept(this));
    }

    public void runAsyncTaskLater(Duration delay) {
        this.task = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> taskConsumer.accept(this), toTicks(delay));
    }

    public void runAsyncTaskTimer(Duration delay, Duration interval) {
        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> taskConsumer.accept(this), toTicks(delay), toTicks(interval));
    }

    public void cancel() {
        if (task != null) {
            this.task.cancel();
        }
    }

    private static long toTicks(Duration duration) {
        return (long) (duration.toMillis() / MS_PER_TICKS);
    }
}
