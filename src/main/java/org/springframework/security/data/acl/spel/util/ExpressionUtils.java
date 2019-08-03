package org.springframework.security.data.acl.spel.util;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

public class ExpressionUtils {

	public static ExpressionParser expression() {
		return new SpelExpressionParser();
	}

	public static EvaluationContext context() {
		EvaluationContext context = SimpleEvaluationContext.forReadWriteDataBinding().build();
		return context;
	}

	public static EvaluationContext context(EvaluationContext context, String name, Object value) {
		EvaluationContext ec = context;
		if (ec == null) {
			ec = context();
		}
		if (value != null) {
			ec.setVariable(name, value);
		}
		return ec;
	}

	public static <T> T values(EvaluationContext context, String expression, Class<T> clazz) {
		ExpressionParser parser = expression();
		T obj = parser.parseExpression(expression).getValue(context, clazz);
		if (obj instanceof Void) {
			return null;
		}
		return obj;
	}
}
