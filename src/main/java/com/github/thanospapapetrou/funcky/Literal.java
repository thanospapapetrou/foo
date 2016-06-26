package com.github.thanospapapetrou.funcky;

import javax.script.ScriptContext;

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
	 *            the Funcky script engine that created this literal or <code>null</code> if this literal was not created by any engine
	 */
	protected Literal(final FunckyScriptEngine engine) {
		super(engine);
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
