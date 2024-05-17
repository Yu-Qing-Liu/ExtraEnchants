package com.github.yuqingliu.extraenchants.enchantment;

import com.github.yuqingliu.extraenchants.utils.TextUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Getter
public abstract class AbstractEnchantment {
    protected final Component name;
    protected final int maxLevel;
    protected final Component description;
    protected final List<Material> applicable;
    protected final List<Component> applicableDisplayNames;
    protected final String requiredLevelFormula;
    protected final String costFormula;

    public Component getLeveledDescription(int level) {
        return TextUtils.replaceComponent(description, "per level", Component.text(""));
    }

    public TextColor getNameColor() {
        return this.name.color();
    }

    public TextColor getDescriptionColor() {
        return this.description.color();
    }

    public abstract int getEnchantmentLevel(ItemStack item);

    public abstract boolean canEnchant(ItemStack item);

    public abstract ItemStack applyEnchantment(ItemStack item, int level);

    public abstract ItemStack removeEnchantment(ItemStack item);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEnchantment that = (AbstractEnchantment) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
