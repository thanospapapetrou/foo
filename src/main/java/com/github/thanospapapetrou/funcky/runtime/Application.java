package com.github.thanospapapetrou.funcky.runtime;

import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedReferenceException;

/**
 * Class representing a Funcky application.
 * 
 * @author thanos
 */
public class Application extends Expression {
	private static final String APPLICATION = "%1$s %2$s";
	private static final String NESTED_APPLICATION = "(%1$s)";

	private final Expression function;
	private final Expression argument;

	/**
	 * Construct a new application.
	 * 
	 * @param engine
	 *            the engine that parsed this application
	 * @param fileName
	 *            the name of the file from which this application was parsed
	 * @param lineNumber
	 *            the number of the line from which this application was parsed
	 * @param function
	 *            the function of this application
	 * @param argument
	 *            the argument of this application
	 * @throws InvalidArgumentException
	 *             if the type of the argument does not match the domain of the function
	 * @throws InvalidFunctionException
	 *             if function is not actually a function
	 * @throws UndefinedReferenceException
	 *             if any undefined reference is encountered during type evaluations
	 */
	public Application(final FunckyScriptEngine engine, final String fileName, final int lineNumber, final Expression function, final Expression argument) throws InvalidArgumentException, InvalidFunctionException, UndefinedReferenceException {
		this(Objects.requireNonNull(engine, "Engine must not be null"), Objects.requireNonNull(fileName, "File name must not be null"), requirePositiveLineNumber(lineNumber), engine.getContext(), function, argument); // TODO check arguments
	}

	Application(final ScriptContext context, final Expression function, final Expression argument) throws InvalidArgumentException, InvalidFunctionException, UndefinedReferenceException {
		this(null, null, 0, context, function, argument);
	}

	private Application(final FunckyScriptEngine engine, final String fileName, final int lineNumber, final ScriptContext context, final Expression function, final Expression argument) throws InvalidArgumentException, InvalidFunctionException, UndefinedReferenceException {
		super(engine, fileName, lineNumber);
		this.function = Objects.requireNonNull(function, "Function must not be null");
		this.argument = Objects.requireNonNull(argument, "Argument must not be null");
		if (!(function.getType(context) instanceof FunctionType)) {
			throw new InvalidFunctionException(function);
		}
		final FunctionType functionType = (FunctionType) function.getType(context);
		final FunckyType argumentType = argument.getType(context);
		if (!argumentType.equals(functionType.getDomain())) {
			throw new InvalidArgumentException(function, functionType.getDomain(), argument, argumentType);
		}
	}

	@Override
	public Literal eval(final ScriptContext context) throws UndefinedReferenceException {
		return ((Function) function.eval(context)).apply(argument, context);
	}

	@Override
	public FunckyType getType(final ScriptContext context) throws UndefinedReferenceException {
		return ((FunctionType) function.getType(context)).getRange();
	}

	@Override
	public String toString() {
		return String.format(APPLICATION, function, (argument instanceof Application) ? String.format(NESTED_APPLICATION, argument) : argument);
	}
}
