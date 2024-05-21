package com.github.yuqingliu.extraenchants.utils;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class MathUtils {
    private static final ExpressionParser parser = new SpelExpressionParser();
    private static final StandardEvaluationContext context = new StandardEvaluationContext();

    public static int evaluateExpression(String expression, int value) {
        context.setVariable("x", value);

        Object result = parser.parseExpression(expression).getValue(context);
        return (Integer) result;
    }
}
