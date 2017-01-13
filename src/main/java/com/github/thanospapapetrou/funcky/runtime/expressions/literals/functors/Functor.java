package com.github.thanospapapetrou.funcky.runtime.expressions.literals.functors;

import java.net.URI;
import java.util.Objects;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.expressions.Expression;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.Function;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.FunctionType;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.Type;

/**
 * Abstract class representing a functor.
 * 
 * @author thanos
 */
public abstract class Functor extends Function implements ApplicableFunctor {
	private static final String DIFFERENT_ARGUMENTS = "Arguments must be exactly %1$d";
	private static final String NON_POSITIVE_ARGUMENTS = "Arguments must be positive";
	private static final String NULL_ARGUMENT = "Argument must not be null";
	private static final String NULL_ARGUMENTS = "Arguments must not be null";

	private final int arguments;

	/**
	 * Construct a new functor.
	 * 
	 * @param engine
	 *            the engine that generated this functor
	 * @param script
	 *            the URI of the script from which this functor was generated
	 * @param name
	 *            the name of this functor
	 * @param type
	 *            the type of this functor
	 * @param arguments
	 *            the arguments of this functor
	 */
	public Functor(final FunckyScriptEngine engine, final URI script, final String name, final FunctionType type, final int arguments) {
		super(engine, script, name, type);
		if (arguments < 1) {
			throw new IllegalArgumentException(NON_POSITIVE_ARGUMENTS);
		}
		this.arguments = arguments;
	}

	@Override
	public Literal apply(final Expression argument) throws ScriptException {
		super.apply(argument);
		final Functor that = this;
		final Type newType = getType().getRange().bind(getType().getDomain().infer(argument.getType().free()));
		return (arguments > 1) ? new Functor(engine, script, toString(), (FunctionType) newType, arguments - 1) { // TODO no casting
			@Override
			public Expression toExpression() {
				return engine.getApplication(that, argument);
			}

			@Override
			public Literal apply(final Expression... arguments) throws ScriptException {
				super.apply(arguments);
				final Expression[] newArguments = new Expression[arguments.length + 1];
				newArguments[0] = argument;
				System.arraycopy(arguments, 0, newArguments, 1, arguments.length);
				return that.apply(newArguments);
			}
		} : apply(new Expression[] {argument});
	}

	@Override
	public Literal apply(final Expression... arguments) throws ScriptException {
		if (Objects.requireNonNull(arguments, NULL_ARGUMENTS).length != this.arguments) {
			throw new IllegalArgumentException(String.format(DIFFERENT_ARGUMENTS, this.arguments));
		}
		for (final Expression argument : arguments) {
			Objects.requireNonNull(argument, NULL_ARGUMENT);
		}
		return null;
	}
}
