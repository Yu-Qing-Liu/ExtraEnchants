package com.github.yuqingliu.extraenchants.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.repositories.AnvilRepository;
import com.github.yuqingliu.extraenchants.item.ItemImpl;
import com.google.inject.Inject;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AnvilCommand implements CommandExecutor {
    @Inject
    private final Logger logger;
    @Inject
    private final AnvilRepository anvilRepository;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("anvil") && sender instanceof Player) {
            Player player = (Player) sender;
            if (!sender.hasPermission("extraenchants.admin")) {
                logger.sendPlayerErrorMessage(player, "You do not have permission to use this command.");
                return false;
            }
            if(args.length == 1) {
                if(!args[0].equals("add")) {
                    return false;
                }
                ItemStack item1 = player.getInventory().getItemInMainHand();
                ItemStack item2 = player.getInventory().getItemInOffHand();
                if(item1 == null || item1.getType() == Material.AIR) {
                    logger.sendPlayerErrorMessage(player, "No item in main hand");
                    return false;
                }
                if(item2 == null || item2.getType() == Material.AIR) {
                    logger.sendPlayerErrorMessage(player, "No item in off hand");
                    return false;
                }
                anvilRepository.addCombination(new ItemImpl(item1), new ItemImpl(item2));
                return true;
            }
        }
        return false;
    }
}


