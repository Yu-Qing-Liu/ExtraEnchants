package com.github.yuqingliu.extraenchants.blocks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class CustomBlock {
    private double x;
    private double y;
    private double z;
    private String blockType;
    private World world;

    public CustomBlock(int x, int y, int z, String blockType, World world) {
        this.blockType = blockType;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CustomBlock(String data) {
        String[] parts = data.split(",");
        String worldName = parts[0];
        this.x = Double.parseDouble(parts[1]);
        this.y = Double.parseDouble(parts[2]);
        this.z = Double.parseDouble(parts[3]);
        this.blockType = parts[4];
        this.world = Bukkit.getWorld(worldName);
    }

    public CustomBlock(Block block, String blockType) {
        Location blockLocation = block.getLocation();
        this.x = blockLocation.getX();
        this.y = blockLocation.getY();
        this.z = blockLocation.getZ();
        this.world = blockLocation.getWorld();
        this.blockType = blockType;
    }

    public Location getLocation() {
        return new Location(world, this.x, this.y, this.z);
    }

    public String getWorldName() {
        return this.world.getName();
    }

    public String toString() {
        return world.getName() + "," + this.x + "," + this.y + "," + this.z + "," + blockType + "\n";
    }

    // Implementing equals method
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        CustomBlock other = (CustomBlock) obj;
        return Double.compare(other.x, x) == 0 &&
               Double.compare(other.y, y) == 0 &&
               Double.compare(other.z, z) == 0 &&
               blockType.equals(other.blockType) &&
               world.getName().equals(other.world.getName());
    }

    // Implementing hashCode method
    @Override
    public int hashCode() {
        int result = 17;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + blockType.hashCode();
        result = 31 * result + world.getName().hashCode();
        return result;
    }
}
