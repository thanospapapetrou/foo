package com.github.thanospapapetrou.foo.runtime;

import java.util.Objects;

import javax.script.ScriptContext;
import javax.script.ScriptException;

public class Definition {
	private final String name;
	private final Expression expression;
	
	public Definition(final String name, final Expression expression) {
		this.name = Objects.requireNonNull(name, "Name must not be null");
		this.expression = Objects.requireNonNull(expression, "Expression must not be null");
	}
	
	public void eval(final ScriptContext context) throws ScriptException {
		context.setAttribute(name, expression.eval(context), ScriptContext.ENGINE_SCOPE);
	}
}