package com.github.thanospapapetrou.funcky.runtime;

import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;

/**
 * Class representing a Funcky application.
 * 
 * @author thanos
 */
public class Application extends Expression {
	private static final String APPLICATION = "%1$s %2$s";
	private static final String NESTED_APPLICATION = "(%1$s)";
	private static final String NULL_FUNCTION = "Function must not be null";
	private static final String NULL_ARGUMENT = "Argument must not be null";
	private static final String NULL_CONTEXT = "Context must not be null";

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
	 * @throws UndefinedSymbolException
	 *             if any undefined reference is encountered during type evaluations
	 */
	public Application(final FunckyScriptEngine engine, final String fileName, final int lineNumber, final Expression function, final Expression argument) throws InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		super(requireNonNullEngine(engine), requireValidFileName(fileName), requirePositiveLineNumber(lineNumber));
		this.function = Objects.requireNonNull(function, NULL_FUNCTION);
		this.argument = Objects.requireNonNull(argument, NULL_ARGUMENT);
		if (!(function.getType(engine.getContext()) instanceof FunctionType)) {
			throw new InvalidFunctionException(function);
		}
		final FunctionType functionType = (FunctionType) function.getType(engine.getContext());
		final FunckyType argumentType = argument.getType(engine.getContext());
		if (functionType.getDomain().inferGenericBindings(argumentType) == null) {
			throw new InvalidArgumentException(function, functionType.getDomain(), argument, argumentType);
		}

	}

	Application(final Expression function, final Expression argument) {
		super(null, null, 0);
		this.function = Objects.requireNonNull(function, NULL_FUNCTION);
		this.argument = Objects.requireNonNull(argument, NULL_ARGUMENT);
	}

	@Override
	public Literal eval(final ScriptContext context) throws UndefinedSymbolException {
		return ((Function) function.eval(Objects.requireNonNull(context, NULL_CONTEXT))).apply(argument, context);
	}

	@Override
	public FunckyType getType(final ScriptContext context) throws UndefinedSymbolException {
		final FunctionType functionType = (FunctionType) function.getType(Objects.requireNonNull(context, NULL_CONTEXT));
		return functionType.getRange().bind(functionType.getDomain().inferGenericBindings(argument.getType(context)));
	}

	@Override
	public String toString() {
		final Expression argumentExpression = (argument instanceof Literal) ? ((Literal) argument).toExpression() : argument;
		return String.format(APPLICATION, function, (argumentExpression instanceof Application) ? String.format(NESTED_APPLICATION, argumentExpression) : argumentExpression);
	}
}
