package com.github.yuqingliu.extraenchants.api.managers;

import org.bukkit.entity.Player;

public interface SoundManager {
    void playEnchantmentSound(Player player);
    void playAnvilSound(Player player);
    void playArrowSound(Player player);
    void playSonicBoomSound(Player player);
    void playMitigationSound(Player player);
    void playGrindstoneSound(Player player);
}
