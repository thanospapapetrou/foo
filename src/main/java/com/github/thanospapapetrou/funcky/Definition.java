package com.github.thanospapapetrou.funcky;

import java.util.Objects;

import javax.script.ScriptContext;
import javax.script.ScriptException;

/**
 * Class representing a Funcky definition.
 * 
 * @author thanos
 */
public class Definition {
	private final String name;
	private final Expression expression;

	Definition(final String name, final Expression expression) {
		this.name = Objects.requireNonNull(name, "Name must not be null");
		this.expression = Objects.requireNonNull(expression, "Expression must not be null");
	}

	/**
	 * Establish this definition in the given script context.
	 * 
	 * @param context
	 *            the script context in which to establish this definition
	 * @throws ScriptException
	 *             if any errors occur during the definition
	 */
	public void eval(final ScriptContext context) throws ScriptException {
		context.setAttribute(name, expression.eval(context), ScriptContext.ENGINE_SCOPE);
	}
	
	String getName() {
		return name;
	}
	
	Expression getExpression() {
		return expression;
	}
}
