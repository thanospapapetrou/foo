package com.github.thanospapapetrou.funcky.runtime;

import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedReferenceException;

/**
 * Class representing a Funcky definition.
 * 
 * @author thanos
 */
public class Definition extends AbstractSyntaxTreeNode {
	private final String name;
	private final Expression expression;

	/**
	 * Construct a new definition.
	 * 
	 * @param engine
	 *            the Funcky script engine that parsed this definition
	 * @param fileName
	 *            the name of the file from which this definition was parsed
	 * @param lineNumber
	 *            the number of the line from which this definition was parsed
	 * @param name
	 *            the name of this definition.
	 * @param expression
	 *            the expression of this definition
	 */
	public Definition(final FunckyScriptEngine engine, final String fileName, final int lineNumber, final String name, final Expression expression) {
		super(engine, fileName, lineNumber);
		this.name = Objects.requireNonNull(name, "Name must not be null");
		this.expression = Objects.requireNonNull(expression, "Expression must not be null");
	}

	@Override
	public Void eval(final ScriptContext context) throws UndefinedReferenceException {
		context.setAttribute(name, expression.eval(context), ScriptContext.ENGINE_SCOPE);
		return null;
	}

	/**
	 * Get the expression.
	 * 
	 * @return the expression of this definition
	 */
	public Expression getExpression() {
		return expression;
	}

	/**
	 * Get the name.
	 * 
	 * @return the name of this definition
	 */
	public String getName() {
		return name;
	}
}
