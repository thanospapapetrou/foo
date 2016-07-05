package com.github.thanospapapetrou.funcky.runtime;

import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedReferenceException;

/**
 * Class representing a Funcky reference.
 * 
 * @author thanos
 */
public class Reference extends Expression {
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
		super(Objects.requireNonNull(engine, "Engine must not be null"), Objects.requireNonNull(fileName, "File name must not be null"), requirePositiveLineNumber(lineNumber));
		this.name = Objects.requireNonNull(name, "Name must not be null");
	}

	Reference(final String name) {
		super(null, null, 0);
		this.name = Objects.requireNonNull(name, "Name must not be null");
	}

	@Override
	public Literal eval(final ScriptContext context) throws UndefinedReferenceException {
		final Object object = context.getAttribute(name);
		if (object instanceof Literal) {
			return (Literal) object;
		}
		throw new UndefinedReferenceException(this);
	}

	@Override
	public FunckyType getType(final ScriptContext context) throws UndefinedReferenceException {
		return eval(context).getType();
	}

	@Override
	public String toString() {
		return name;
	}
}
