package com.github.yuqingliu.extraenchants.managers;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.github.yuqingliu.extraenchants.api.managers.SoundManager;
import com.google.inject.Singleton;

@Singleton
public class SoundManagerImpl implements SoundManager {
    @Override
    public void playEnchantmentSound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
    }

    @Override
    public void playAnvilSound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
    }

    @Override
    public void playArrowSound(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0f, 1.0f);
    }

    @Override
    public void playSonicBoomSound(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0F, 1.0F);
    }
    
    @Override
    public void playMitigationSound(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
    }
}
