package com.github.yuqingliu.extraenchants.managers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.managers.CommandManager;
import com.github.yuqingliu.extraenchants.api.managers.NameSpacedKeyManager;
import com.github.yuqingliu.extraenchants.api.repositories.AnvilRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.commands.AnvilCommand;
import com.github.yuqingliu.extraenchants.commands.EnchantCommand;
import com.github.yuqingliu.extraenchants.commands.EnchantableCommand;
import com.github.yuqingliu.extraenchants.commands.LevelCommand;
import com.github.yuqingliu.extraenchants.commands.ListCommand;
import com.github.yuqingliu.extraenchants.commands.WandCommand;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CommandManagerImpl implements CommandManager {
    private final JavaPlugin plugin;
    private final Logger logger;
    private final NameSpacedKeyManager nameSpacedKeyManager;
    private final EnchantmentRepository enchantmentRepository;
    private final AnvilRepository anvilRepository;
    private Map<String, CommandExecutor> commands = new HashMap<>();
        
    @Inject
    public CommandManagerImpl(
        JavaPlugin plugin,
        Logger logger,
        NameSpacedKeyManager nameSpacedKeyManager,
        EnchantmentRepository enchantmentRepository,
        AnvilRepository anvilRepository) {
        this.plugin = plugin;
        this.logger = logger;
        this.nameSpacedKeyManager = nameSpacedKeyManager;
        this.enchantmentRepository = enchantmentRepository;
        this.anvilRepository = anvilRepository;
    }
    
    @Inject
    private void postConstruct() {
        initializeCommands();
        registerCommands();
    }

    private void initializeCommands() {
        commands.put("wand", new WandCommand(logger, nameSpacedKeyManager));
        commands.put("list", new ListCommand(logger, enchantmentRepository));
        commands.put("enchant", new EnchantCommand(logger, enchantmentRepository));
        commands.put("level", new LevelCommand(logger, enchantmentRepository));
        commands.put("anvil", new AnvilCommand(logger, anvilRepository));
        commands.put("enchantable", new EnchantableCommand(logger, enchantmentRepository));
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
