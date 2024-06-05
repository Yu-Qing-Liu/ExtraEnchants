package com.github.yuqingliu.extraenchants.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandExecutor;

import com.github.yuqingliu.extraenchants.ExtraEnchantsImpl;
import com.github.yuqingliu.extraenchants.api.command.CommandManager;

public class CommandManagerImpl implements CommandManager {
    private final ExtraEnchantsImpl plugin;
    private Map<String, CommandExecutor> commands = new HashMap<>();
        
    public CommandManagerImpl(ExtraEnchantsImpl plugin) {
        this.plugin = plugin;
        initializeCommands();
        registerCommands();
    }

    private void initializeCommands() {
        commands.put("ee", new EECommand(plugin));
        commands.put("eeget", new EEGetCommand());
        commands.put("eelist", new EEListCommand(plugin));
    }

    private void registerCommands() {
        for(Map.Entry<String, CommandExecutor> entry : commands.entrySet()) {
            String name = entry.getKey();
            CommandExecutor command = entry.getValue();
            registerCommand(name, command);
        }
    }

    @Override
    public CommandExecutor getCommand(String name) {
        return commands.get(name);
    }

    @Override
    public void registerCommand(String name, CommandExecutor command) {
        plugin.getCommand(name).setExecutor(command);
    }
}

