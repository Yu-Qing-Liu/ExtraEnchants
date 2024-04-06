package com.github.yuqingliu.extraenchants.utils;

public class Enchantment {
    private final String name;
    private final int maxLevel;

    public Enchantment(String name, int maxLevel) {
        this.name = name;
        this.maxLevel = maxLevel;
    }

    public String getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    // Define any behavior methods here, such as applying the enchantment to an item
    // and checking compatibility with other enchantments.
    
    public boolean conflictsWith(Enchantment other) {
        // Implement logic to determine if this enchantment conflicts with another
        return other.getName().equals(this.name);
    }
    
    // Additional methods as needed for your custom enchantment logic
}

