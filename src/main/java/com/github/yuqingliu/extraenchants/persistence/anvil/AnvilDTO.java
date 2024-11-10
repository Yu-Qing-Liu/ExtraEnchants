package com.github.yuqingliu.extraenchants.persistence.anvil;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.item.ItemImpl;

import lombok.Getter;

@Getter
public class AnvilDTO {
    @JsonDeserialize(as = ItemImpl.class)
    private final Item item;
    @JsonDeserialize(contentAs = ItemImpl.class)
    private final Set<Item> combinable;

    public AnvilDTO(
        @JsonProperty("item") Item item,
        @JsonProperty("combinable") Set<Item> combinable) {
        this.item = item;
        this.combinable = combinable;
    }
}
