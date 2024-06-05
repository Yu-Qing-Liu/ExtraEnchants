package com.github.yuqingliu.extraenchants.api.command;

import org.bukkit.command.CommandExecutor;

import lombok.NonNull;

public interface CommandManager {
    void registerCommand(String name, CommandExecutor command);

    @NonNull CommandExecutor getCommand(String name);
}

