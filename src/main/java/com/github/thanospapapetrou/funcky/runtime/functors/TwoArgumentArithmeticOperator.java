package com.github.thanospapapetrou.funcky.runtime.functors;

import java.net.URI;
import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.exceptions.AlreadyDefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.literals.FunckyNumber;
import com.github.thanospapapetrou.funcky.runtime.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.literals.types.FunckyType;

/**
 * Abstract class representing a two argument arithmetic operator.
 * 
 * @author thanos
 */
public abstract class TwoArgumentArithmeticOperator extends Functor {
	private static final String NULL_CONTEXT = "Context must not be null";
	private static final String NULL_ARGUMENT_1 = "Argument 1 must not be null";
	private static final String NULL_ARGUMENT_2 = "Argument 2 must not be null";

	/**
	 * Construct a new operator.
	 * 
	 * @param engine
	 * @param script
	 * @param name
	 *            the name of this operator
	 * @param numberType
	 *            the number type used to define the type of this operator
	 */
	public TwoArgumentArithmeticOperator(final FunckyScriptEngine engine, final URI script, final String name, final FunckyType numberType) {
		super(engine, script, name, numberType, numberType, numberType);
	}

	@Override
	protected Literal apply(final ScriptContext context, final Expression... arguments) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		super.apply(context, arguments);
		return apply((FunckyNumber) arguments[0].eval(context), (FunckyNumber) arguments[1].eval(context), context);
	}

	/**
	 * Apply this operator to the given arguments.
	 * 
	 * @param argument1
	 *            the first argument to apply this operator to
	 * @param argument2
	 *            the second argument to apply this operator to
	 * @param context
	 *            the context in which to evaluate the application
	 * @return the literal result of applying this operator to the given arguments
	 */
	protected Literal apply(final FunckyNumber argument1, final FunckyNumber argument2, final ScriptContext context) {
		Objects.requireNonNull(argument1, NULL_ARGUMENT_1);
		Objects.requireNonNull(argument2, NULL_ARGUMENT_2);
		Objects.requireNonNull(context, NULL_CONTEXT);
		return null;
	}
}
