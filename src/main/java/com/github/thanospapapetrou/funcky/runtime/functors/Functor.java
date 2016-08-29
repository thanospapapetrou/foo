package com.github.thanospapapetrou.funcky.runtime.functors;

import java.net.URI;
import java.util.Arrays;
import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.Application;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.FunckyType;
import com.github.thanospapapetrou.funcky.runtime.Function;
import com.github.thanospapapetrou.funcky.runtime.FunctionType;
import com.github.thanospapapetrou.funcky.runtime.Literal;
import com.github.thanospapapetrou.funcky.runtime.exceptions.AlreadyDefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;

/**
 * Abstract class representing a functor.
 * 
 * @author thanos
 */
public abstract class Functor extends Function {
	private static final String DIFFERENT_ARGUMENTS = "Arguments must be exactly %1$d";
	private static final int FUNCTION_TYPES = 2;
	private static final String LESS_TYPES = "Types must not be less than %1$d";
	private static final String NULL_ARGUMENT = "Argument must not be null";
	private static final String NULL_ARGUMENTS = "Arguments must not be null";
	private static final String NULL_CONTEXT = "Context must not be null";
	private static final String NULL_TYPE = "Type %1$d must not be null";
	private static final String NULL_TYPES = "Types must not be null";

	private final FunckyType[] types;

	private static FunctionType getFunctionType(final FunckyScriptEngine engine, final URI script, final FunckyType... types) {
		return new FunctionType(engine, types[0], (types.length == FUNCTION_TYPES) ? types[1] : getFunctionType(engine, script, Arrays.copyOfRange(types, 1, types.length)));
	}

	private static FunckyType[] requireValidTypes(final FunckyType[] types) {
		if ((Objects.requireNonNull(types, NULL_TYPES)).length < FUNCTION_TYPES) {
			throw new IllegalArgumentException(String.format(LESS_TYPES, FUNCTION_TYPES));
		}
		for (int i = 0; i < types.length; i++) {
			Objects.requireNonNull(types[i], String.format(NULL_TYPE, i));
		}
		return types;
	}

	/**
	 * Construct a new functor.
	 * 
	 * @param engine
	 * @param script
	 * @param lineNumber
	 * @param name
	 * @param types
	 */
	public Functor(final FunckyScriptEngine engine, final URI script, final int lineNumber, final String name, final FunckyType... types) {
		super(engine, script, lineNumber, name, getFunctionType(engine, script, requireValidTypes(types)));
		this.types = types;
	}

	@Override
	public Literal apply(final Expression argument, final ScriptContext context) throws InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException, AlreadyDefinedSymbolException {
		super.apply(argument, context);
		final Functor that = this;
		final FunckyType[] newTypes = new FunckyType[types.length - 1];
		for (int i = 0; i < newTypes.length; i++) {
			newTypes[i] = types[i + 1].bind(types[0].inferGenericBindings(argument.getType(context))); // TODO unbind unbound type variables
		}
		return (types.length == FUNCTION_TYPES) ? apply(context, argument) : new Functor(engine, script, lineNumber, toString(), newTypes) {
			@Override
			public Expression toExpression() {
				return new Application(engine, script, 0, that, argument); // TODO
			}

			@Override
			public String toString() {
				return toExpression().toString();
			}

			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
				super.apply(context, arguments);
				final Expression[] newArguments = new Expression[arguments.length + 1];
				newArguments[0] = argument;
				System.arraycopy(arguments, 0, newArguments, 1, arguments.length);
				return that.apply(context, newArguments);
			}
		};
	}

	/**
	 * Apply this functor to the given arguments.
	 * 
	 * @param context
	 *            the context in which to evaluate the application
	 * @param arguments
	 *            the arguments to apply this functor to
	 * @return the literal result of applying this functor to the given arguments
	 * @throws InvalidArgumentException
	 *             if any invalid argument is encountered
	 * @throws InvalidFunctionException
	 *             if any invalid function is encountered
	 * @throws UndefinedSymbolException
	 *             if any reference to an undefined symbol is encountered
	 * @throws AlreadyDefinedSymbolException
	 *             if a definition for an already defined symbol is encountered
	 */
	protected Literal apply(final ScriptContext context, final Expression... arguments) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		Objects.requireNonNull(context, NULL_CONTEXT);
		if (Objects.requireNonNull(arguments, NULL_ARGUMENTS).length != (types.length - 1)) {
			throw new IllegalArgumentException(String.format(DIFFERENT_ARGUMENTS, types.length - 1));
		}
		for (final Expression argument : arguments) {
			Objects.requireNonNull(argument, NULL_ARGUMENT);
		}
		return null;
	}
}
