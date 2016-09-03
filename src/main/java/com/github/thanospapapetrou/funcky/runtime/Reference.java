package com.github.thanospapapetrou.funcky.runtime;

import java.net.URI;
import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;

/**
 * Class representing a Funcky reference.
 * 
 * @author thanos
 */
public class Reference extends Expression {
	private static final String EMPTY_NAME = "Name must not be empty";
	private static final String NULL_CONTEXT = "Context must not be null";
	private static final String NULL_NAME = "Name must not be null";

	private final String name;

	/**
	 * Construct a new reference.
	 * 
	 * @param engine
	 *            the engine that generated this reference
	 * @param script
	 *            the URI of the script from which this reference was generated
	 * @param lineNumber
	 *            the number of the line from which this reference was parsed or <code>0</code> if this reference was not parsed (is builtin or generated at runtime)
	 * @param name
	 *            the name of this reference
	 */
	public Reference(final FunckyScriptEngine engine, final URI script, final int lineNumber, final String name) {
		super(engine, script, lineNumber);
		if ((this.name = Objects.requireNonNull(name, NULL_NAME)).isEmpty()) {
			throw new IllegalArgumentException(EMPTY_NAME);
		}
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
	public FunckyType getType(final ScriptContext context) throws InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		return eval(Objects.requireNonNull(context, NULL_CONTEXT)).getType(context);
	}

	@Override
	public String toString() {
		return name;
	}
}
