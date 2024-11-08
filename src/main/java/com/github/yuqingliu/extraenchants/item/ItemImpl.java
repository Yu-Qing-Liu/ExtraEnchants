package com.github.yuqingliu.extraenchants.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.item.Item;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class ItemImpl implements Item {
    @EqualsAndHashCode.Include
    private Material material;
    @EqualsAndHashCode.Include
    private Component displayName;

    public ItemImpl(ItemStack itemStack) {
        this.material = itemStack.getType();
        this.displayName = itemStack.displayName();
    }
}
