package com.github.thanospapapetrou.foo.runtime;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.foo.FooScriptEngine;

/**
 * Abstract class representing a Foo literal.
 * 
 * @author thanos
 */
public abstract class Literal extends Expression {
	/**
	 * Construct a new literal.
	 * 
	 * @param engine
	 *            the Foo script engine that created this literal or <code>null</code> if this literal was not created by any engine
	 */
	public Literal(final FooScriptEngine engine) {
		super(engine);
	}

	@Override
	public Literal eval(final ScriptContext _) {
		return this;
	}

	@Override
	public FooType getType(final ScriptContext _) {
		return getType();
	}

	/**
	 * Get the type of this literal.
	 * 
	 * @return the type of this literal
	 */
	protected abstract FooType getType();
}
