package com.github.yuqingliu.extraenchants.persistence.blocks;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class LocationDTO {
    private final String worldName;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public LocationDTO(
        @JsonProperty("worldName") String worldName,
        @JsonProperty("x") double x,
        @JsonProperty("y") double y,
        @JsonProperty("z") double z,
        @JsonProperty("yaw") float yaw,
        @JsonProperty("pitch") float pitch) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public LocationDTO(Location location) {
        this.worldName = location.getWorld().getName();
        this.x = location.x();
        this.y = location.y();
        this.z = location.z();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }
}
