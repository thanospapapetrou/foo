package com.github.thanospapapetrou.foo.runtime;

import javax.script.ScriptContext;

/**
 * Abstract class representing a Foo literal.
 * 
 * @author thanos
 */
public abstract class Literal implements Command {
	@Override
	public Literal eval(final ScriptContext _) {
		return this;
	}

	@Override
	public SimpleType getType(final ScriptContext _) {
		return getType();
	}

	/**
	 * Get the type of this literal.
	 * 
	 * @return the type of this literal
	 */
	protected abstract SimpleType getType();
}
