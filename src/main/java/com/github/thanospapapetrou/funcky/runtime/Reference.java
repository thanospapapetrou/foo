package com.github.thanospapapetrou.funcky.runtime;

import java.util.Objects;

import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;

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
	 *            the script engine that parsed this reference
	 * @param name
	 *            the name of this reference
	 */
	public Reference(final FunckyScriptEngine engine, final String name) {
		super(Objects.requireNonNull(engine, "Engine must not be null"));
		this.name = Objects.requireNonNull(name, "Name must not be null");
	}

	@Override
	public Literal eval(final ScriptContext context) throws ScriptException {
		final Object object = context.getAttribute(name);
		if (object instanceof Literal) {
			return (Literal) object;
		}
		throw new ScriptException(String.format("Reference %1$s is undefined", name));
	}

	@Override
	public FunckyType getType(final ScriptContext context) throws ScriptException {
		return eval(context).getType();
	}

	@Override
	public String toString() {
		return name;
	}
}
