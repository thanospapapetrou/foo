package com.github.thanospapapetrou.funcky.runtime;

import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;

/**
 * Class representing a Funcky reference.
 * 
 * @author thanos
 */
public class Reference extends Expression {
	private static final String NULL_NAME = "Name must not be null";
	private static final String NULL_CONTEXT = "Context must not be null";

	private final String name;

	/**
	 * Construct a new reference.
	 * 
	 * @param engine
	 *            the Funcky script engine that parsed this reference
	 * @param fileName
	 *            the name of the file from which this reference was parsed
	 * @param lineNumber
	 *            the number of the line from which this reference was parsed
	 * @param name
	 *            the name of this reference
	 */
	public Reference(final FunckyScriptEngine engine, final String fileName, final int lineNumber, final String name) {
		super(requireNonNullEngine(engine), requireValidFileName(fileName), requirePositiveLineNumber(lineNumber));
		this.name = Objects.requireNonNull(name, NULL_NAME);
	}

	Reference(final String name) {
		super(null, null, 0);
		this.name = Objects.requireNonNull(name, NULL_NAME);
	}

	@Override
	public Literal eval(final ScriptContext context) throws UndefinedSymbolException {
		final Object object = Objects.requireNonNull(context, NULL_CONTEXT).getAttribute(name);
		if (object instanceof Literal) {
			return (Literal) object;
		}
		throw new UndefinedSymbolException(this);
	}

	@Override
	public FunckyType getType(final ScriptContext context) throws UndefinedSymbolException {
		return eval(Objects.requireNonNull(context, NULL_CONTEXT)).getType();
	}

	@Override
	public String toString() {
		return name;
	}
}
