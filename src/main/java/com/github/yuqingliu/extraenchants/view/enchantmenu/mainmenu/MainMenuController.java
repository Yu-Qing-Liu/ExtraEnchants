package com.github.yuqingliu.extraenchants.view.enchantmenu.mainmenu;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import com.github.yuqingliu.extraenchants.view.enchantmenu.EnchantMenu;

public class MainMenuController {
    private final EnchantMenu enchantMenu;
    private Map<Player, int[]> pageNumbers = new ConcurrentHashMap<>();

    public MainMenuController(EnchantMenu enchantMenu) {
        this.enchantMenu = enchantMenu;
    }

    public void onClose(Player player) {
        pageNumbers.remove(player);
    }
}
