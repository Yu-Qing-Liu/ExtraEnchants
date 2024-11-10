package com.github.yuqingliu.extraenchants.api.weapon;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface Weapon {
    void setDamage(double damage);
    double getDamage();
    void applyHit(Player user, LivingEntity target);
}

