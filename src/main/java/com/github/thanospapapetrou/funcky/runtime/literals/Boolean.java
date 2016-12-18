package com.github.thanospapapetrou.funcky.runtime.literals;

import java.net.URI;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.Reference;
import com.github.thanospapapetrou.funcky.runtime.libraries.Booleans;
import com.github.thanospapapetrou.funcky.runtime.literals.types.SimpleType;

/**
 * Class representing a Funcky boolean.
 * 
 * @author thanos
 */
public class Boolean extends Literal {
	private final boolean value;

	/**
	 * Construct a new boolean.
	 * 
	 * @param engine
	 *            the engine that generated this boolean
	 * @param script
	 *            the URI of the script from which this boolean was generated
	 * @param value
	 *            the value of this boolean
	 */
	public Boolean(final FunckyScriptEngine engine, final URI script, final boolean value) {
		super(engine, script, -1);
		this.value = value;
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof Boolean) && (value == ((Boolean) object).value);
	}

	@Override
	public SimpleType getType() throws ScriptException {
		return engine.getLiteral(Booleans.class, Booleans.BOOLEAN);
	}

	@Override
	public int hashCode() {
		return java.lang.Boolean.valueOf(value).hashCode();
	}

	@Override
	public Reference toExpression() {
		return engine.getReference(Booleans.class, java.lang.Boolean.toString(value));
	}
}
