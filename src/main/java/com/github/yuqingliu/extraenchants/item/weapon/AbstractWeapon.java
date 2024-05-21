package com.github.yuqingliu.extraenchants.item.weapon;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class AbstractWeapon {
    protected ItemStack weapon;
    @Setter protected double damage;

    public AbstractWeapon(ItemStack weapon) {
        this.weapon = weapon;
    }

    public abstract double calculateBaseDamage();

    public abstract void applyHit(Player weaponUser, LivingEntity victim);
}
