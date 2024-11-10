package com.github.yuqingliu.extraenchants.persistence.blocks;

import java.util.Set;

import org.bukkit.Location;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class BlocksDTO {
    private final Set<Location> blocks;
    
    public BlocksDTO(
        @JsonProperty("blocks") Set<Location> blocks) {
        this.blocks = blocks;
    }
}
