package com.github.thanospapapetrou.funcky.runtime.literals;

import java.net.URI;
import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.Reference;
import com.github.thanospapapetrou.funcky.runtime.exceptions.AlreadyDefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.literals.types.FunctionType;

/**
 * Abstract class representing a Funcky function.
 * 
 * @author thanos
 */
public abstract class Function extends Literal {
	private static final String NULL_ARGUMENT = "Argument must not be null";
	private static final String NULL_CONTEXT = "Context must not be null";

	private final String name;
	private final FunctionType type;

	/**
	 * Construct a new function.
	 * 
	 * @param engine
	 *            the engine that defined this function
	 * @param script
	 *            the URI of the script that this function was parsed from
	 * @param lineNumber
	 *            the number of the line of the script that this function was parsed from
	 * @param name
	 *            the name of this function
	 * @param type
	 *            the type of this function
	 */
	public Function(final FunckyScriptEngine engine, final URI script, final int lineNumber, final String name, final FunctionType type) {
		super(engine, script, lineNumber);
		this.name = name;
		this.type = type;
	}

	/**
	 * Apply this function to an argument.
	 * 
	 * @param argument
	 *            the argument to apply this function to
	 * @param context
	 *            the context in which to evaluate the application
	 * @return the literal result of applying this function to the given argument
	 * @throws InvalidArgumentException
	 *             if the type of the argument does not match the domain of the function
	 * @throws InvalidFunctionException
	 *             if function is not actually a function
	 * @throws UndefinedSymbolException
	 *             if any reference to an undefined symbol is encountered
	 * @throws AlreadyDefinedSymbolException
	 *             if a definition for an already defined symbol is encountered
	 */
	public Literal apply(final Expression argument, final ScriptContext context) throws InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException, AlreadyDefinedSymbolException {
		Objects.requireNonNull(argument, NULL_ARGUMENT);
		Objects.requireNonNull(context, NULL_CONTEXT);
		return null;
	}

	@Override
	public FunctionType getType(final ScriptContext context) throws InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		super.getType(context);
		return type;
	}

	@Override
	public Expression toExpression() {
		return new Reference(engine, name);
	}

	@Override
	public String toString() {
		return name;
	}
}
