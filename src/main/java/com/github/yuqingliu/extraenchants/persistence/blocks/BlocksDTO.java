package com.github.yuqingliu.extraenchants.persistence.blocks;

import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Location;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BlocksDTO {
    private Set<LocationDTO> blocks;
    
    public BlocksDTO(
        @JsonProperty("blocks") Set<LocationDTO> blocks) {
        this.blocks = blocks;
    }

    public void setBlocks(Set<Location> blocks) {
        this.blocks = blocks.stream().map(location -> new LocationDTO(location)).collect(Collectors.toSet());
    }

    public Set<Location> getBlocks() {
        return blocks.stream().map(block -> block.toLocation()).collect(Collectors.toSet());
    }
}

