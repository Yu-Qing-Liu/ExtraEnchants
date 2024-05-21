package com.github.yuqingliu.extraenchants.item.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.yuqingliu.extraenchants.item.ItemUtils.Rarity;
import com.github.yuqingliu.extraenchants.item.lore.Lore;
import com.github.yuqingliu.extraenchants.item.lore.implementations.DescriptionSection;
import com.github.yuqingliu.extraenchants.item.lore.implementations.RaritySection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;

@AllArgsConstructor
@Getter
public class Item {
    private final Material material;
    @Setter Rarity rarity;
    @Setter private Component displayName;
    @Setter private Component description;
    
    public ItemStack getItem() {
        ItemStack item = new ItemStack(material);

        ItemMeta meta = item.getItemMeta();
        meta.displayName(displayName);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_DYE);
        meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        item.setItemMeta(meta);

        Lore lore = new Lore(item);
        DescriptionSection descriptionSection = (DescriptionSection) lore.getLoreMap().get("DescriptionSection").getDefinition();
        RaritySection raritySection = (RaritySection) lore.getLoreMap().get("RaritySection").getDefinition();
        descriptionSection.updateSection(description);
        raritySection.updateSection(rarity);
        item = lore.applyLore();
        return item;
    }
}
