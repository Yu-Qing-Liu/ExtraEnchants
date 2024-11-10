package com.github.yuqingliu.extraenchants.managers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.managers.CommandManager;
import com.github.yuqingliu.extraenchants.api.managers.NameSpacedKeyManager;
import com.github.yuqingliu.extraenchants.commands.WandCommand;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CommandManagerImpl implements CommandManager {
    private final JavaPlugin plugin;
    private final Logger logger;
    private final NameSpacedKeyManager nameSpacedKeyManager;
    private Map<String, CommandExecutor> commands = new HashMap<>();
        
    @Inject
    public CommandManagerImpl(
        JavaPlugin plugin,
        Logger logger,
        NameSpacedKeyManager nameSpacedKeyManager) {
        this.plugin = plugin;
        this.logger = logger;
        this.nameSpacedKeyManager = nameSpacedKeyManager;
    }
    
    @Inject
    private void postConstruct() {
        initializeCommands();
        registerCommands();
    }

    private void initializeCommands() {
        commands.put("wand", new WandCommand(logger, nameSpacedKeyManager));
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
