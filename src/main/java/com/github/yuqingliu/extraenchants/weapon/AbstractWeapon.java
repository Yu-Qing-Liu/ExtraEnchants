package com.github.yuqingliu.extraenchants.weapon;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.weapon.Weapon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public abstract class AbstractWeapon implements Weapon {
    protected final ItemStack weapon;
    @Getter @Setter protected double damage;

    protected abstract double calculateBaseDamage();

    @Override
    public abstract void applyHit(Player user, LivingEntity target);
}
