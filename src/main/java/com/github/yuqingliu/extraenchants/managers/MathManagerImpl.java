package com.github.yuqingliu.extraenchants.managers;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import com.github.yuqingliu.extraenchants.api.managers.MathManager;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MathManagerImpl implements MathManager {
    private final Random random = new Random();
    
    @Override
    public int evaluateExpression(String expression, int variableValue) {
        Expression exp = new ExpressionBuilder(expression)
            .variables("x")
            .build()
            .setVariable("x", variableValue);
        double result = exp.evaluate();
        return (int) result;
    }

    @Override
    public Location getRightSide(Location location, double distance) {
        float angle = location.getYaw() / 60;
        return location.clone().subtract(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
    }

    @Override
    public Vector getRandomizedDirection(Vector originalDirection, double spread) {
        double offsetX = (random.nextDouble() - 0.5) * spread;
        double offsetY = (random.nextDouble() - 0.5) * spread;
        double offsetZ = (random.nextDouble() - 0.5) * spread;
        return originalDirection.add(new Vector(offsetX, offsetY, offsetZ)).normalize();
    }
    
    @Override
    public Vector getRandomPointOnEntityHitbox(Entity target) {
        BoundingBox boundingBox = target.getBoundingBox();
        double minX = boundingBox.getMinX();
        double maxX = boundingBox.getMaxX();
        double minY = boundingBox.getMinY();
        double maxY = boundingBox.getMaxY();
        double minZ = boundingBox.getMinZ();
        double maxZ = boundingBox.getMaxZ();
        double randomX = minX + (maxX - minX) * random.nextDouble();
        double randomY = minY + (maxY - minY) * random.nextDouble();
        double randomZ = minZ + (maxZ - minZ) * random.nextDouble();
        return new Vector(randomX, randomY, randomZ);
    }
}
