package com.github.thanospapapetrou.funcky.runtime;

import java.net.URI;
import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.AlreadyDefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedReferenceException;

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
	 * @param lineNumber
	 *            the number of the line from which this definition was parsed or <code>0</code> if this expression was not parsed (is builtin or generated at runtime)
	 * @param name
	 *            the name of this definition.
	 * @param expression
	 *            the expression of this definition
	 */
	public Definition(final FunckyScriptEngine engine, final URI script, final int lineNumber, final String name, final Expression expression) {
		super(engine, script, lineNumber);
		if ((this.name = Objects.requireNonNull(name, NULL_NAME)).isEmpty()) {
			throw new IllegalArgumentException(EMPTY_NAME);
		}
		this.expression = Objects.requireNonNull(expression, NULL_EXPRESSION);
	}

	@Override
	public Void eval(final ScriptContext context) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedReferenceException {
		super.eval(context);
		if (context.getAttribute(name) == null) {
			context.setAttribute(name, expression, ScriptContext.ENGINE_SCOPE);
			return null;
		}
		throw new AlreadyDefinedSymbolException(this);
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
