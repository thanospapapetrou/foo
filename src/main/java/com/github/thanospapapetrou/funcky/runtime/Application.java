package com.github.thanospapapetrou.funcky.runtime;

import java.net.URI;
import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.AlreadyDefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedReferenceException;
import com.github.thanospapapetrou.funcky.runtime.literals.Function;
import com.github.thanospapapetrou.funcky.runtime.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.literals.types.FunctionType;
import com.github.thanospapapetrou.funcky.runtime.literals.types.Type;

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

	private final Expression function;
	private final Expression argument;

	/**
	 * Construct a new application.
	 * 
	 * @param engine
	 *            the engine that generated this application
	 * @param script
	 *            the URI of the script from which this application was generated
	 * @param line
	 *            the line from which this application was parsed or <code>0</code> if this application was not parsed (is builtin or generated at runtime)
	 * @param function
	 *            the function of this application
	 * @param argument
	 *            the argument of this application
	 */
	public Application(final FunckyScriptEngine engine, final URI script, final int line, final Expression function, final Expression argument) {
		super(engine, script, line);
		this.function = Objects.requireNonNull(function, NULL_FUNCTION);
		this.argument = Objects.requireNonNull(argument, NULL_ARGUMENT);
	}

	/**
	 * Construct a new application generated at runtime.
	 * 
	 * @param engine
	 *            the engine that generated this application
	 * @param function
	 *            the function of this application
	 * @param argument
	 *            the argument of this application
	 */
	public Application(final FunckyScriptEngine engine, final Expression function, final Expression argument) {
		this(engine, FunckyScriptEngine.RUNTIME, 0, function, argument);
	}
	
	@Override
	public boolean equals(final Object object) {
		if (object instanceof Application) {
			final Application application = (Application) object;
			return function.equals(application.function) && argument.equals(application.argument);
		}
		return false;
	}

	@Override
	public Literal eval(final ScriptContext context) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedReferenceException {
		super.eval(context);
		checkTypes(context);
		return ((Function) function.eval(context)).apply(argument, context);
	}

	/**
	 * Get the argument of this application.
	 * 
	 * @return the argument of this application
	 */
	public Expression getArgument() {
		return argument;
	}

	/**
	 * Get the function of this application.
	 * 
	 * @return the function of this application
	 */
	public Expression getFunction() {
		return function;
	}

	@Override
	public Type getType(final ScriptContext context) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedReferenceException {
		super.getType(context);
		checkTypes(context);
		final FunctionType functionType = (FunctionType) function.getType(context);
		return functionType.getRange().bind(functionType.getDomain().inferGenericBindings(argument.getType(context).free()));
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(function, argument);
	}

	@Override
	public String toString() {
		final Expression argumentExpression = (argument instanceof Literal) ? ((Literal) argument).toExpression() : argument;
		return String.format(APPLICATION, function, (argumentExpression instanceof Application) ? String.format(NESTED_APPLICATION, argumentExpression) : argumentExpression);
	}

	private void checkTypes(final ScriptContext context) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedReferenceException {
		if (!(function.getType(context) instanceof FunctionType)) {
			throw new InvalidFunctionException(function);
		}
		if (((FunctionType) function.getType(context)).getDomain().inferGenericBindings(argument.getType(context).free()) == null) {
			throw new InvalidArgumentException(context, this);
		}
	}
}
