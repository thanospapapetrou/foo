package com.github.thanospapapetrou.funcky.runtime;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;

/**
 * Abstract class representing a Funcky literal.
 * 
 * @author thanos
 */
public abstract class Literal extends Expression {
	/**
	 * Construct a new literal.
	 * 
	 * @param engine
	 *            the Funcky script engine that parsed this literal or <code>null</code> if this literal was not parsed by any engine
	 * @param fileName
	 *            the name of the file from which this literal was parsed or <code>null</code> if this literal was not parsed from any file
	 * @param lineNumber
	 *            the number of the line from which this literal was parsed or <code>0</code> if this literal was not parsed from any line
	 */
	protected Literal(final FunckyScriptEngine engine, final String fileName, final int lineNumber) {
		super(engine, fileName, lineNumber);
	}

	@Override
	public Literal eval(final ScriptContext context) {
		return this;
	}

	@Override
	public FunckyType getType(final ScriptContext context) {
		return getType();
	}

	/**
	 * Get the type of this literal.
	 * 
	 * @return the type of this literal
	 */
	protected abstract FunckyType getType();
}
