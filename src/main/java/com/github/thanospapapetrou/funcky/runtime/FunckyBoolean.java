package com.github.thanospapapetrou.funcky.runtime;

import java.net.URI;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;

/**
 * Class representing a Funcky boolean.
 * 
 * @author thanos
 */
public class FunckyBoolean extends Literal {
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
	public FunckyBoolean(final FunckyScriptEngine engine, final URI script, final boolean value) {
		super(engine, script, 0);
		this.value = value;
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof FunckyBoolean) && (value == ((FunckyBoolean) object).value);
	}

	@Override
	public SimpleType getType(final ScriptContext context) throws InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		super.getType(context);
		return engine.getPrelude().getBoolean();
	}

	@Override
	public int hashCode() {
		return Boolean.valueOf(value).hashCode();
	}

	@Override
	public Expression toExpression() {
		return value ? engine.getPrelude().getTrue() : engine.getPrelude().getFalse();
	}

	@Override
	public String toString() {
		return Boolean.toString(value);
	}
}
