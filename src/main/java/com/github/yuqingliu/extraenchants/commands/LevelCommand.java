package com.github.yuqingliu.extraenchants.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.google.inject.Inject;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LevelCommand implements CommandExecutor {
    @Inject
    private final Logger logger;
    @Inject
    private final EnchantmentRepository enchantmentRepository;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("level") && sender instanceof Player) {
            Player player = (Player) sender;
            if (!sender.hasPermission("extraenchants.admin")) {
                logger.sendPlayerErrorMessage(player, "You do not have permission to use this command.");
                return false;
            }
            if(args.length == 3) {
                if(!args[0].equals("set")) {
                    return false;
                }
                EnchantID id;
                int maxLevel = 0;
                try {
                    id = EnchantID.valueOf(args[1]);
                } catch (Exception e) {
                    logger.sendPlayerErrorMessage(player, "Not a valid EnchantID. Use /list to get a list of valid IDs");
                    return false;
                }
                try {
                    maxLevel = Integer.parseInt(args[2]);
                } catch (Exception e) {
                    logger.sendPlayerErrorMessage(player, "Not a valid integer for maxLevel paramger.");
                    return false;
                }
                enchantmentRepository.setEnchantmentMaxLevel(id, maxLevel);
                return true;
            }
        }
        return false;
    }
}


