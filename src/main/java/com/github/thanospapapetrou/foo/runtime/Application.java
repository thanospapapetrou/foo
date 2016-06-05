package com.github.thanospapapetrou.foo.runtime;

import java.util.Objects;

import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.github.thanospapapetrou.foo.FooScriptEngine;

/**
 * Class representing a Foo application.
 * 
 * @author thanos
 */
public class Application extends Expression {
	private final Expression function;
	private final Expression argument;

	/**
	 * Construct a new application.
	 * 
	 * @param engine
	 *            the Foo script engine that created this application
	 * @param function
	 *            the function to apply
	 * @param argument
	 *            the argument to apply the given function to
	 * @throws ScriptException
	 */
	public Application(final FooScriptEngine engine, final Expression function, final Expression argument) throws ScriptException {
		super(Objects.requireNonNull(engine, "Engine must not be null"));
		this.function = Objects.requireNonNull(function, "Function must not be null");
		this.argument = Objects.requireNonNull(argument, "Argument must not be null");
		final FooType functionType = function.getType(engine.getContext());
		final FooType argumentType = argument.getType(engine.getContext());
		if (!(functionType instanceof FunctionType)) {
			throw new ScriptException(String.format("%1$s can not be applied to %2$s: %2$s is not a function", argument, function));
		}
		if (!argumentType.equals(((FunctionType) functionType).getDomain())) {
			throw new ScriptException(String.format("%1$s can not be applied to function %2$s: %2$s has type %3$s and %1$s has type %4$s", argument, function, functionType, argumentType));
		}
	}

	@Override
	public Literal eval(final ScriptContext context) throws ScriptException {
		return ((Function) function.eval(context)).apply(argument);
	}

	@Override
	public FooType getType(final ScriptContext context) throws ScriptException {
		return ((FunctionType) function.getType(context)).getRange();
	}
}
