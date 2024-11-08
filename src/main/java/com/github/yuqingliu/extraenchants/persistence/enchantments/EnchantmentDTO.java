package com.github.yuqingliu.extraenchants.persistence.enchantments;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.item.ItemImpl;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

@Getter
public class EnchantmentDTO {
    private EnchantID enchantID;
    private Component name;
    private Component description;
    private Duration cooldown;
    private int maxLevel;

    @JsonDeserialize(contentAs = ItemImpl.class)
    private Set<Item> applicable;

    private Set<EnchantID> conflicting;
    private String requiredLevelFormula;
    private String costFormula;
    private List<TextColor> leveledColors;

    @JsonCreator
    public EnchantmentDTO(
            @JsonProperty("enchantID") EnchantID enchantID,
            @JsonProperty("name") Component name,
            @JsonProperty("description") Component description,
            @JsonProperty("cooldown") Duration cooldown,
            @JsonProperty("maxLevel") int maxLevel,
            @JsonProperty("applicable") Set<Item> applicable,
            @JsonProperty("conflicting") Set<EnchantID> conflicting,
            @JsonProperty("requiredLevelFormula") String requiredLevelFormula,
            @JsonProperty("costFormula") String costFormula,
            @JsonProperty("leveledColors") List<TextColor> leveledColors) {
        this.enchantID = enchantID;
        this.name = name;
        this.description = description;
        this.cooldown = cooldown;
        this.maxLevel = maxLevel;
        this.applicable = applicable;
        this.conflicting = conflicting;
        this.requiredLevelFormula = requiredLevelFormula;
        this.costFormula = costFormula;
        this.leveledColors = leveledColors;
    }
}
