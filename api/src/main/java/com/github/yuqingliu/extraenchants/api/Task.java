package com.github.yuqingliu.extraenchants.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;

public class Task {
    private final static double MS_PER_TICKS = 50;
    private final Plugin plugin;
    private final Consumer<Task> taskConsumer;
    @Getter private BukkitTask task;
    private Instant startTime;

    public Task(Plugin plugin, Consumer<Task> taskConsumer) {
        this.plugin = plugin;
        this.taskConsumer = taskConsumer;
        this.startTime = Instant.now();
    }

    public void runTask() {
        this.task = Bukkit.getScheduler().runTask(plugin, () -> taskConsumer.accept(this));
    }

    public void runTaskLater(Duration delay) {
        this.task = Bukkit.getScheduler().runTaskLater(plugin, () -> taskConsumer.accept(this), toTicks(delay));
    }

    public void runTaskTimer(Duration delay, Duration interval) {
        this.task = Bukkit.getScheduler().runTaskTimer(plugin, () -> taskConsumer.accept(this), toTicks(delay), toTicks(interval));
        startTime = Instant.now();
    }

    public void runAsyncTask() {
        this.task = Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> taskConsumer.accept(this));
    }

    public void runAsyncTaskLater(Duration delay) {
        this.task = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> taskConsumer.accept(this), toTicks(delay));
    }

    public void runAsyncTaskTimer(Duration delay, Duration interval) {
        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> taskConsumer.accept(this), toTicks(delay), toTicks(interval));
        startTime = Instant.now();
    }

    public void cancel() {
        if (task != null) {
            this.task.cancel();
        }
    }

    public void cancelAfter(Duration duration, Runnable runnable) {
        if (Duration.between(startTime, Instant.now()).compareTo(duration) >= 0) {
            cancel();
            if(runnable != null) {
                runnable.run();
            }
        }
    }

    public void cancelAfter(Duration duration) {
        if (Duration.between(startTime, Instant.now()).compareTo(duration) >= 0) {
            cancel();
        }
    }

    private static long toTicks(Duration duration) {
        return (long) (duration.toMillis() / MS_PER_TICKS);
    }
}
