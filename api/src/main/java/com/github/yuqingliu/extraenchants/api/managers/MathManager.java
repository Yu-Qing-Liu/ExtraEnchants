package com.github.yuqingliu.extraenchants.api.managers;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public interface MathManager {
    int evaluateExpression(String expression, int variableValue);
    Vector getRandomizedDirection(Vector originalDirection, double spread);
    Location getRightSide(Location location, double distance);
    Vector getRandomPointOnEntityHitbox(Entity target);
}
