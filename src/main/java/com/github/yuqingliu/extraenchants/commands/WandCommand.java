package com.github.yuqingliu.extraenchants.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.managers.NameSpacedKeyManager;
import com.github.yuqingliu.extraenchants.view.anvilmenu.AnvilMenu;
import com.github.yuqingliu.extraenchants.view.enchantmenu.EnchantMenu;
import com.github.yuqingliu.extraenchants.view.grindstonemenu.GrindstoneMenu;
import com.google.inject.Inject;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@RequiredArgsConstructor
public class WandCommand implements CommandExecutor {
    @Inject
    private final Logger logger;
    @Inject
    private final NameSpacedKeyManager nameSpacedKeyManager;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("wand") && sender instanceof Player) {
            Player player = (Player) sender;
            if (!sender.hasPermission("extraenchants.admin")) {
                logger.sendPlayerErrorMessage(player, "You do not have permission to use this command.");
                return false;
            }
            if(args.length == 1) {
                ItemStack wand = new ItemStack(Material.STICK);
                ItemMeta meta = wand.getItemMeta();
                switch(args[0]) {
                    case "etable" -> {
                        meta.displayName(Component.text("Enchanting Table Wand", NamedTextColor.AQUA));
                        wand.setItemMeta(meta);
                        meta = wand.getItemMeta();
                        PersistentDataContainer container = meta.getPersistentDataContainer();
                        container.set(nameSpacedKeyManager.getCustomUIKey(), PersistentDataType.STRING, EnchantMenu.class.getSimpleName());
                        wand.setItemMeta(meta);
                        player.getInventory().addItem(wand);
                    }
                    case "anvil" -> {
                        meta.displayName(Component.text("Anvil Wand", NamedTextColor.AQUA));
                        wand.setItemMeta(meta);
                        meta = wand.getItemMeta();
                        PersistentDataContainer container = meta.getPersistentDataContainer();
                        container.set(nameSpacedKeyManager.getCustomUIKey(), PersistentDataType.STRING, AnvilMenu.class.getSimpleName());
                        wand.setItemMeta(meta);
                        player.getInventory().addItem(wand);
                    }
                    case "grindstone" -> {
                        meta.displayName(Component.text("Grindstone Wand", NamedTextColor.AQUA));
                        wand.setItemMeta(meta);
                        meta = wand.getItemMeta();
                        PersistentDataContainer container = meta.getPersistentDataContainer();
                        container.set(nameSpacedKeyManager.getCustomUIKey(), PersistentDataType.STRING, GrindstoneMenu.class.getSimpleName());
                        wand.setItemMeta(meta);
                        player.getInventory().addItem(wand);
                    }
                    default -> {
                        logger.sendPlayerErrorMessage(player, "Invalid GUI Type. Types: etable, anvil, grindstone");
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}


