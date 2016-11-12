package com.github.thanospapapetrou.funcky.runtime;

import java.net.URI;
import java.util.Objects;

import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.AlreadyDefinedSymbolException;

/**
 * Class representing a Funcky definition.
 * 
 * @author thanos
 */
public class Definition extends AbstractSyntaxTreeNode {
	private static final String EMPTY_NAME = "Name must not be empty";
	private static final String NULL_EXPRESSION = "Expression must not be null";
	private static final String NULL_NAME = "Name must not be null";

	private final String name;
	private final Expression expression;

	/**
	 * Construct a new definition.
	 * 
	 * @param engine
	 *            the engine that generated this definition
	 * @param script
	 *            the URI of the script from which this definition was generated
	 * @param line
	 *            the line from which this definition was parsed or <code>0</code> if this definition was not parsed (is builtin or generated at runtime)
	 * @param name
	 *            the name of this definition.
	 * @param expression
	 *            the expression of this definition
	 */
	public Definition(final FunckyScriptEngine engine, final URI script, final int line, final String name, final Expression expression) {
		super(engine, script, line);
		if ((this.name = Objects.requireNonNull(name, NULL_NAME)).isEmpty()) {
			throw new IllegalArgumentException(EMPTY_NAME);
		}
		this.expression = Objects.requireNonNull(expression, NULL_EXPRESSION);
	}

	@Override
	public Void eval(final ScriptContext context) throws ScriptException {
		super.eval(context);
		final int scope = engine.getScope(context, script);
		if (context.getAttribute(name, scope) != null) {
			throw new AlreadyDefinedSymbolException(this);
		}
		context.setAttribute(name, expression, scope);
		return null;
	}

	/**
	 * Get the name.
	 * 
	 * @return the name of this definition
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the expression.
	 * 
	 * @return the expression of this definition
	 */
	public Expression getExpression() {
		return expression;
	}
}
