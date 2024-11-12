package com.github.yuqingliu.extraenchants.commands;

import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.Rarity;
import com.google.inject.Inject;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@RequiredArgsConstructor
public class ListCommand implements CommandExecutor {
    @Inject
    private final Logger logger;
    @Inject
    private final EnchantmentRepository enchantmentRepository;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("list") && sender instanceof Player) {
            Player player = (Player) sender;
            if (!sender.hasPermission("extraenchants.admin")) {
                logger.sendPlayerErrorMessage(player, "You do not have permission to use this command.");
                return false;
            }
            if(args.length == 0) {
                Map<Enchantment, Integer> sortedEnchants = enchantmentRepository.getSortedEnchantments();
                Component message = Component.empty();
                Rarity prev = null;
                for(Map.Entry<Enchantment, Integer> entry : sortedEnchants.entrySet()) {
                    Rarity current = entry.getKey().getId().rarity();
                    Component line = Component.empty();
                    if(current != prev) {
                        line = line.append(Component.newline()).append(Component.text(current + " Enchants: ", current.color())).append(Component.newline()).append(Component.newline());
                    }
                    line = line.append(Component.text("EnchantID: ", NamedTextColor.WHITE))
                    .append(Component.text(entry.getKey().getId().name(), current.color()))
                    .append(Component.text(" Max Level: "))
                    .append(Component.text(entry.getValue(), NamedTextColor.YELLOW))
                    .append(Component.newline());
                    message = message.append(line);
                    prev = current;
                }
                player.sendMessage(message);
                return true;
            }
        }
        return false;
    }
}


