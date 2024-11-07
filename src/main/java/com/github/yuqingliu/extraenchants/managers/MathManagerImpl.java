package com.github.yuqingliu.extraenchants.managers;

import com.github.yuqingliu.extraenchants.api.managers.MathManager;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MathManagerImpl implements MathManager {
    public int evaluateExpression(String expression, int variableValue) {
        Expression exp = new ExpressionBuilder(expression)
            .variables("x")
            .build()
            .setVariable("x", variableValue);
        double result = exp.evaluate();
        return (int) result;
    }
}
