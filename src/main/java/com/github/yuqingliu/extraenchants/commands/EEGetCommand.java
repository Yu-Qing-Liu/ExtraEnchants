package com.github.yuqingliu.extraenchants.commands;

import com.github.yuqingliu.extraenchants.ExtraEnchants;
import com.github.yuqingliu.extraenchants.blocks.CustomBlockUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import de.tr7zw.changeme.nbtapi.NBTItem;

public class EEGetCommand implements CommandExecutor {
    private final ExtraEnchants plugin;

    public EEGetCommand(ExtraEnchants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("eeget") && sender instanceof Player) {
            Player player = (Player) sender;

            if (!sender.hasPermission("extraenchants.eeget")) {
                player.sendMessage(Component.text("You do not have permission to use this command.", NamedTextColor.RED));
                return true;
            }

            // Check command arguments
            if (args.length < 1) {
                player.sendMessage(Component.text("Usage: /eeget <menu>", NamedTextColor.RED));
                return true;
            }

            String menu = args[0];

            switch (menu) {
                case "anvil":
                    CustomBlockUtils.giveCustomAnvil(player);
                    break;
                case "etable":
                    CustomBlockUtils.giveCustomEtable(player);
                    break;
                case "grindstone":
                    CustomBlockUtils.giveCustomGrindstone(player);
                    break;
                default:
                    player.sendMessage(Component.text("Incorrect menu type, menu types: {anvil, etable, grindstone}", NamedTextColor.RED));
                    break;
            }
            return true;
        }
        return false;
    }
}
