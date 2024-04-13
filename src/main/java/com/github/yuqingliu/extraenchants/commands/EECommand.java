package com.github.yuqingliu.extraenchants.commands;

import com.github.yuqingliu.extraenchants.enchants.utils.UtilityMethods;
import com.github.yuqingliu.extraenchants.enchants.Database;
import com.github.yuqingliu.extraenchants.enchants.utils.CustomEnchantment;
import com.github.yuqingliu.extraenchants.ExtraEnchants;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EECommand implements CommandExecutor {
    private final ExtraEnchants plugin;

    public EECommand(ExtraEnchants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ee") && sender instanceof Player) {
            Player player = (Player) sender;

            if (!sender.hasPermission("extraenchants.ee")) {
                player.sendMessage(Component.text("You do not have permission to use this command.", NamedTextColor.RED));
                return true;
            }

            // Check command arguments
            if (args.length < 2) {
                player.sendMessage(Component.text("Usage: /ee <enchantment> <level>", NamedTextColor.RED));
                return true;
            }

            String enchantmentName = args[0];
            int level;

            try {
                level = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(Component.text("The level must be a valid number.", NamedTextColor.RED));
                return true;
            }

            ItemStack item = player.getInventory().getItemInMainHand();
            if (item == null || item.getType() == Material.AIR) {
                player.sendMessage(Component.text("You must be holding an item to enchant.", NamedTextColor.RED));
                return true;
            }

            // Attempt to add the enchantment
            List<CustomEnchantment> Registry = Database.getCustomEnchantmentRegistry();
            CustomEnchantment enchantment = null;
            for(CustomEnchantment enchant : Registry) {
                if(enchantmentName.equals(enchant.getName())) enchantment = enchant;
            }
            ItemStack finalItem = null;
            if(enchantment != null && enchantment.getAddCmd() == null || enchantment.getAddCmd().isEmpty()) {
                finalItem = UtilityMethods.addEnchantment(item, enchantmentName, level, enchantment.getColor(), true);
            }

            if (finalItem != null) {
                player.getInventory().setItemInMainHand(finalItem);
                player.sendMessage(Component.text("Enchantment applied successfully!", NamedTextColor.GREEN));
            } else {
                player.sendMessage(Component.text("Failed to apply enchantment. Check enchantment name and level limits.", NamedTextColor.RED));
            }

            return true;
        }
        return false;
    }
}

