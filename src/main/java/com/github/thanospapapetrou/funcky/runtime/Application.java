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
	 */
	public Application(final FunckyScriptEngine engine, final String fileName, final int lineNumber, final Expression function, final Expression argument) {
		super(requireNonNullEngine(engine), requireValidFileName(fileName), requirePositiveLineNumber(lineNumber));
		// TODO is engine needed any more in super()?
		this.function = Objects.requireNonNull(function, NULL_FUNCTION);
		this.argument = Objects.requireNonNull(argument, NULL_ARGUMENT);
	}

	Application(final Expression function, final Expression argument) {
		super(null, null, 0);
		this.function = Objects.requireNonNull(function, NULL_FUNCTION);
		this.argument = Objects.requireNonNull(argument, NULL_ARGUMENT);
	}

	@Override
	public Literal eval(final ScriptContext context) throws InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		checkType(Objects.requireNonNull(context, NULL_CONTEXT));
		return ((Function) function.eval(context)).apply(argument, context);
	}

	@Override
	public FunckyType getType(final ScriptContext context) throws InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		checkType(Objects.requireNonNull(context, NULL_CONTEXT));
		final FunctionType functionType = (FunctionType) function.getType(context);
		return functionType.getRange().bind(functionType.getDomain().inferGenericBindings(argument.getType(context)));
	}

	@Override
	public String toString() {
		final Expression argumentExpression = (argument instanceof Literal) ? ((Literal) argument).toExpression() : argument;
		return String.format(APPLICATION, function, (argumentExpression instanceof Application) ? String.format(NESTED_APPLICATION, argumentExpression) : argumentExpression);
	}

	private void checkType(final ScriptContext context) throws InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		if (!(function.getType(context) instanceof FunctionType)) {
			throw new InvalidFunctionException(function);
		}
		final FunctionType functionType = (FunctionType) function.getType(context);
		final FunckyType argumentType = argument.getType(context);
		if (functionType.getDomain().inferGenericBindings(argumentType) == null) {
			throw new InvalidArgumentException(function, functionType.getDomain(), argument, argumentType);
		}
	}
}
