package com.github.thanospapapetrou.funcky.runtime.literals;

import java.net.URI;
import java.util.Objects;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.literals.types.FunctionType;

/**
 * Abstract class representing a Funcky function.
 * 
 * @author thanos
 */
public abstract class Function extends Literal implements ApplicableFunction {
	private static final String EMPTY_NAME = "Name must not be null";
	private static final String NULL_ARGUMENT = "Argument must not be null";
	private static final String NULL_NAME = "Name must not be null";
	private static final String NULL_TYPE = "Type must not be null";

	private final String name;
	private final FunctionType type;

	/**
	 * Construct a new function.
	 * 
	 * @param engine
	 *            the engine that defined this function
	 * @param script
	 *            the URI of the script that this function was parsed from
	 * @param name
	 *            the name of this function
	 * @param type
	 *            the type of this function
	 */
	public Function(final FunckyScriptEngine engine, final URI script, final String name, final FunctionType type) {
		super(engine, script, -1);
		if (Objects.requireNonNull(name, NULL_NAME).isEmpty()) {
			throw new IllegalArgumentException(EMPTY_NAME);
		}
		this.name = name;
		this.type = Objects.requireNonNull(type, NULL_TYPE);
	}

	@Override
	public Literal apply(final Expression argument) throws ScriptException {
		Objects.requireNonNull(argument, NULL_ARGUMENT);
		return null;
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof Function) && toExpression().equals(((Function) object).toExpression());
	}

	@Override
	public FunctionType getType() throws ScriptException {
		return type;
	}

	@Override
	public int hashCode() {
		return toExpression().hashCode();
	}

	@Override
	public Expression toExpression() {
		return engine.getReference(script, name);
	}
}
