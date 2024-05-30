package com.github.yuqingliu.extraenchants.api.utils;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MathUtils {
    public static int evaluateExpression(String expression, int value) {
        Expression exp = new ExpressionBuilder(expression)
            .variables("x")
            .build()
            .setVariable("x", value); // Set value for x

        // Evaluate the expression
        double result = exp.evaluate();
        return (int) result;
    }
}
