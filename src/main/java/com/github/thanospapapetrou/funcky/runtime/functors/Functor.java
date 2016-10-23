package com.github.thanospapapetrou.funcky.runtime.functors;

import java.net.URI;
import java.util.Arrays;
import java.util.Objects;

import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.literals.Function;
import com.github.thanospapapetrou.funcky.runtime.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.literals.types.FunctionType;
import com.github.thanospapapetrou.funcky.runtime.literals.types.Type;

/**
 * Abstract class representing a functor.
 * 
 * @author thanos
 */
public abstract class Functor extends Function implements ApplicableFunctor {
	private static final String DIFFERENT_ARGUMENTS = "Arguments must be exactly %1$d";
	private static final int FUNCTION_TYPES = 2;
	private static final String LESS_TYPES = "Types must not be less than %1$d";
	private static final String NULL_ARGUMENT = "Argument must not be null";
	private static final String NULL_ARGUMENTS = "Arguments must not be null";
	private static final String NULL_CONTEXT = "Context must not be null";
	private static final String NULL_TYPE = "Type %1$d must not be null";
	private static final String NULL_TYPES = "Types must not be null";

	private final Type[] types;

	private static FunctionType getFunctionType(final FunckyScriptEngine engine, final URI script, final Type... types) {
		return new FunctionType(engine, types[0], (types.length == FUNCTION_TYPES) ? types[1] : getFunctionType(engine, script, Arrays.copyOfRange(types, 1, types.length)));
	}

	private static Type[] requireValidTypes(final Type[] types) {
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
	 *            the engine that generated this functor
	 * @param script
	 *            the URI of the script from which this functor was generated
	 * @param name
	 *            the name of this functor
	 * @param types
	 *            the types of this functor
	 */
	public Functor(final FunckyScriptEngine engine, final URI script, final String name, final Type... types) {
		super(engine, script, name, getFunctionType(engine, script, requireValidTypes(types)));
		this.types = types;
	}

	@Override
	public Literal apply(final Expression argument, final ScriptContext context) throws ScriptException {
		super.apply(argument, context);
		final Functor that = this;
		final Type[] newTypes = new Type[types.length - 1];
		for (int i = 0; i < newTypes.length; i++) {
			newTypes[i] = types[i + 1].bind(types[0].inferGenericBindings(argument.getType(context).free()));
		}
		return (types.length == FUNCTION_TYPES) ? apply(context, argument) : new Functor(engine, script, toString(), newTypes) {
			@Override
			public Expression toExpression() {
				return engine.getApplication(that, argument);
			}

			@Override
			public Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				super.apply(context, arguments);
				final Expression[] newArguments = new Expression[arguments.length + 1];
				newArguments[0] = argument;
				System.arraycopy(arguments, 0, newArguments, 1, arguments.length);
				return that.apply(context, newArguments);
			}
		};
	}

	@Override
	public Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
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
