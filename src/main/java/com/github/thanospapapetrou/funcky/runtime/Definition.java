package com.github.thanospapapetrou.funcky.runtime;

import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.AlreadyDefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;

/**
 * Class representing a Funcky definition.
 * 
 * @author thanos
 */
public class Definition extends AbstractSyntaxTreeNode {
	private static final String NULL_NAME = "Name must not be null";
	private static final String NULL_EXPRESSION = "Expression must not be null";
	private static final String NULL_CONTEXT = "Context must not be null";

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
		super(requireNonNullEngine(engine), requireValidFileName(fileName), requirePositiveLineNumber(lineNumber));
		this.name = Objects.requireNonNull(name, NULL_NAME);
		this.expression = Objects.requireNonNull(expression, NULL_EXPRESSION);
	}

	@Override
	public Void eval(final ScriptContext context) throws AlreadyDefinedSymbolException, UndefinedSymbolException {
		if (Objects.requireNonNull(context, NULL_CONTEXT).getAttribute(name) == null) {
			context.setAttribute(name, expression.eval(context), ScriptContext.ENGINE_SCOPE);
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
}
