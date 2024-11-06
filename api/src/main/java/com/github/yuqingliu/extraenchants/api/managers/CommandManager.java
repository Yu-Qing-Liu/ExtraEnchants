package com.github.yuqingliu.extraenchants.api.managers;

import org.bukkit.command.CommandExecutor;

public interface CommandManager {
    void registerCommand(String name, CommandExecutor command);
    CommandExecutor getCommand(String name);
}
