package com.github.thanospapapetrou.funcky.runtime.expressions.literals.functors;

import java.net.URI;
import java.util.Objects;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.expressions.Expression;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.Number;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.SimpleType;

/**
 * Abstract class representing a two argument arithmetic operator.
 * 
 * @author thanos
 */
public abstract class TwoArgumentArithmeticOperator extends Functor implements ApplicableTwoArgumentArithmeticOperator {
	private static final int ARGUMENTS = 2;
	private static final String NULL_ARGUMENT_1 = "Argument 1 must not be null";
	private static final String NULL_ARGUMENT_2 = "Argument 2 must not be null";

	/**
	 * Construct a new operator.
	 * 
	 * @param engine
	 *            the engine that generated this operator
	 * @param script
	 *            the URI of the script from which this operator was generated
	 * @param name
	 *            the name of this operator
	 * @param numberType
	 *            the number type used to define the type of this operator
	 */
	public TwoArgumentArithmeticOperator(final FunckyScriptEngine engine, final URI script, final String name, final SimpleType numberType) {
		super(engine, script, name, engine.getFunctionType(numberType, engine.getFunctionType(numberType, numberType)), ARGUMENTS);
	}

	@Override
	public Literal apply(final Expression... arguments) throws ScriptException {
		super.apply(arguments);
		return apply((Number) arguments[0].eval(), (Number) arguments[1].eval());
	}

	@Override
	public Number apply(final Number argument1, final Number argument2) {
		Objects.requireNonNull(argument1, NULL_ARGUMENT_1);
		Objects.requireNonNull(argument2, NULL_ARGUMENT_2);
		return null;
	}
}
