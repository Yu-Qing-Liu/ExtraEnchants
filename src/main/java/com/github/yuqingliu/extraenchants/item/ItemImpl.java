package com.github.yuqingliu.extraenchants.item;

import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.item.Item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ItemImpl implements Item {
    private final ItemStack itemStack;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ItemImpl other = (ItemImpl) obj;
        return this.itemStack.isSimilar(other.itemStack);
    }

    @Override
    public int hashCode() {
        return itemStack != null ? itemStack.hashCode() : 0;
    }
}
