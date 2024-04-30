package com.github.yuqingliu.extraenchants.commands;

import com.github.yuqingliu.extraenchants.enchants.Constants;
import com.github.yuqingliu.extraenchants.ExtraEnchants;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class EEListCommand implements CommandExecutor {
    private final ExtraEnchants plugin;

    public EEListCommand(ExtraEnchants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("eelist") && sender instanceof Player) {
            Player player = (Player) sender;

            if (!sender.hasPermission("extraenchants.eelist")) {
                player.sendMessage(Component.text("You do not have permission to use this command.", NamedTextColor.RED));
                return true;
            }

            // Check command arguments
            if (args.length != 0) {
                player.sendMessage(Component.text("Usage: /eelist", NamedTextColor.RED));
                return true;
            }

            // Attempt list the enchantments [Name:enchname, MaxLevel: maxlevel]
            try {
                HashMap<String, List<Object>> CustomEnchantmentRegistry = Constants.getCustomEnchantments();
                StringBuilder messageBuilder = new StringBuilder("Custom Enchantments:\n");
                
                for (Map.Entry<String, List<Object>> entry : CustomEnchantmentRegistry.entrySet()) {
                    String enchantName = entry.getKey();
                    int maxLevel = (int) CustomEnchantmentRegistry.get(enchantName).get(0);
                    // Append enchantment name and max level to the message
                    messageBuilder.append("Name: ").append(enchantName).append(", MaxLevel: ").append(maxLevel).append("\n");
                }
                
                // Send the message to the player
                player.sendMessage(Component.text(messageBuilder.toString()));
                
                return true;
            } catch (Exception e) {
                player.sendMessage(Component.text("Failed to List enchantments", NamedTextColor.RED));
            }
        }
        return false;
    }
}

