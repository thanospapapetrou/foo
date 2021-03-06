package com.github.thanospapapetrou.funcky.runtime.libraries;

import java.net.URI;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.expressions.Expression;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.ApplicableFunction;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.Boolean;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.Number;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.functors.ApplicableTwoArgumentArithmeticOperator;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.functors.TwoArgumentArithmeticOperator;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.SimpleType;

/**
 * Numbers related library.
 * 
 * @author thanos
 */
public class Numbers extends Library {
	/**
	 * Add two numbers.
	 */
	public static final String ADD = "add";

	/**
	 * Divide two numbers.
	 */
	public static final String DIVIDE = "divide";

	/**
	 * Number e (2.7182818284590452354).
	 */
	public static final String E = "e";

	/**
	 * Infinity (∞).
	 */
	public static final String INFINITY = "infinity";

	/**
	 * Get the integer part of a number.
	 */
	public static final String INTEGER = "integer";

	/**
	 * Check if a number is NaN.
	 */
	public static final String IS_NAN = "isNaN";

	/**
	 * Negate a number.
	 */
	public static final String MINUS = "minus";

	/**
	 * Multiply two numbers.
	 */
	public static final String MULTIPLY = "multiply";

	/**
	 * Not a number (NaN).
	 */
	public static final String NAN = "NaN";

	/**
	 * Type of numbers.
	 */
	public static final String NUMBER = "number";

	/**
	 * Number π (3.14159265358979323846).
	 */
	public static final String PI = "pi";

	/**
	 * Subtract two numbers.
	 */
	public static final String SUBTRACT = "subtract";

	private static final URI NUMBERS = Library.getUri(Numbers.class);

	private final SimpleType numberType;

	/**
	 * Construct a new numbers library.
	 * 
	 * @param engine
	 *            the engine constructing this numbers library
	 * @throws ScriptException
	 *             if any errors occur while constructing this numbers library
	 */
	public Numbers(final FunckyScriptEngine engine) throws ScriptException {
		super(engine);
		numberType = new SimpleType(engine, NUMBERS, NUMBER);
		addDefinition(numberType);
		addDefinition(getNumber(Double.POSITIVE_INFINITY));
		addDefinition(getNumber(Double.NaN));
		addDefinition(PI, getNumber(Math.PI));
		addDefinition(E, getNumber(Math.E));
		addFunctionDefinition(IS_NAN, numberType, engine.getReference(Booleans.class, Booleans.BOOLEAN).evaluate(SimpleType.class), new ApplicableFunction() {
			@Override
			public Boolean apply(final Expression argument) throws ScriptException {
				return engine.getReference(Booleans.class, Double.isNaN(argument.evaluate(Number.class).getValue()) ? Booleans.TRUE : Booleans.FALSE).evaluate(Boolean.class);
			}
		});
		addFunctionDefinition(INTEGER, numberType, numberType, new ApplicableFunction() {
			@Override
			public Number apply(final Expression argument) throws ScriptException {
				final double value = argument.evaluate(Number.class).getValue();
				return engine.getNumber((Double.isInfinite(value) || Double.isNaN(value)) ? value : Double.valueOf(value).intValue());
			}
		});
		addTwoArgumentArithmeticOperatorDefinition(ADD, new ApplicableTwoArgumentArithmeticOperator() {
			@Override
			public Number apply(final Number term1, final Number term2) {
				return engine.getNumber(term1.getValue() + term2.getValue());
			}
		});
		addTwoArgumentArithmeticOperatorDefinition(SUBTRACT, new ApplicableTwoArgumentArithmeticOperator() {
			@Override
			public Number apply(final Number minuend, final Number subtrahend) {
				return engine.getNumber(minuend.getValue() - subtrahend.getValue());
			}
		});
		addTwoArgumentArithmeticOperatorDefinition(MULTIPLY, new ApplicableTwoArgumentArithmeticOperator() {
			@Override
			public Number apply(final Number factor1, final Number factor2) {
				return engine.getNumber(factor1.getValue() * factor2.getValue());
			}
		});
		addTwoArgumentArithmeticOperatorDefinition(DIVIDE, new ApplicableTwoArgumentArithmeticOperator() {
			@Override
			public Number apply(final Number dividend, final Number divisor) {
				return engine.getNumber(dividend.getValue() / divisor.getValue());
			}
		});
	}

	private void addTwoArgumentArithmeticOperatorDefinition(final String name, final ApplicableTwoArgumentArithmeticOperator operator) throws ScriptException {
		addDefinition(new TwoArgumentArithmeticOperator(engine, NUMBERS, name, numberType) {
			@Override
			public Number apply(final Number argument1, final Number argument2) {
				super.apply(argument1, argument2);
				return operator.apply(argument1, argument2);
			}
		});
	}

	private Number getNumber(final double value) {
		return new Number(engine, NUMBERS, -1, value);
	}
}
