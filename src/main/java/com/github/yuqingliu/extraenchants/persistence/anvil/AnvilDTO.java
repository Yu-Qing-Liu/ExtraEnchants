package com.github.yuqingliu.extraenchants.persistence.anvil;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.item.ItemImpl;

import lombok.Getter;

@Getter
public class AnvilDTO {
    @JsonDeserialize(contentAs = ItemImpl.class)
    private final Map<Item, Set<Item>> anvilCombinations;

    public AnvilDTO(
        @JsonProperty("anvilCombinations") Map<Item, Set<Item>> anvilCombinations) {
        this.anvilCombinations = anvilCombinations;
    }
}
