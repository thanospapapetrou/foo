package com.github.thanospapapetrou.funcky;

import java.util.Objects;

import javax.script.ScriptContext;
import javax.script.ScriptException;

/**
 * Class representing a Funcky application.
 * 
 * @author thanos
 */
public class Application extends Expression {
	private final Expression function;
	private final Expression argument;

	Application(final FunckyScriptEngine engine, final Expression function, final Expression argument) throws ScriptException {
		super(Objects.requireNonNull(engine, "Engine must not be null"));
		this.function = Objects.requireNonNull(function, "Function must not be null");
		this.argument = Objects.requireNonNull(argument, "Argument must not be null");
		final FunckyType functionType = function.getType(engine.getContext());
		final FunckyType argumentType = argument.getType(engine.getContext());
		if (!(functionType instanceof FunctionType)) {
			throw new ScriptException(String.format("%1$s can not be applied to %2$s: %2$s is not a function", argument, function));
		}
		if (!argumentType.equals(((FunctionType) functionType).getDomain())) {
			throw new ScriptException(String.format("%1$s can not be applied to %2$s: %2$s has type %3$s and %1$s has type %4$s", argument, function, functionType, argumentType));
		}
	}

	@Override
	public Literal eval(final ScriptContext context) throws ScriptException {
		return ((Function) function.eval(context)).apply(argument, context);
	}

	@Override
	public FunckyType getType(final ScriptContext context) throws ScriptException {
		return ((FunctionType) function.getType(context)).getRange();
	}
}
