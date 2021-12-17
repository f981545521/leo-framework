package cn.acyou.leo.framework.util;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author youfang
 * @version [1.0.0, 2021/12/17 11:24]
 **/
public class ElParser {
    private static final ExpressionParser parser = new SpelExpressionParser();

    /**
     * 得到关键
     * example key : #name
     *
     * @param key        关键
     * @param paramNames 参数名称
     * @param args       参数
     * @return {@link String}
     */
    public static String getKey(String key, String[] paramNames, Object[] args) {
        Expression expression = parser.parseExpression(key);
        StandardEvaluationContext context = new StandardEvaluationContext();
        if (args.length <= 0) {
            return null;
        }
        for (int i = 0; i < args.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        return expression.getValue(context, String.class);
    }
}