package com.github.yuqingliu.extraenchants.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.item.ItemImpl;
import com.google.inject.Inject;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EnchantableCommand implements CommandExecutor {
    @Inject
    private final Logger logger;
    @Inject
    private final EnchantmentRepository enchantmentRepository;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("enchantable") && sender instanceof Player) {
            Player player = (Player) sender;
            if (!sender.hasPermission("extraenchants.admin")) {
                logger.sendPlayerErrorMessage(player, "You do not have permission to use this command.");
                return false;
            }
            if(args.length == 1) {
                EnchantID id;
                try {
                    id = EnchantID.valueOf(args[0]);
                } catch (Exception e) {
                    logger.sendPlayerErrorMessage(player, "Not a valid EnchantID. Use /list to get a list of valid IDs");
                    return false;
                }
                if(player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                    logger.sendPlayerErrorMessage(player, "No item in main hand to enchant");
                    return false;
                }
                enchantmentRepository.addApplicable(id, new ItemImpl(player.getInventory().getItemInMainHand()));
                return true;
            }
        }
        return false;
    }
}


