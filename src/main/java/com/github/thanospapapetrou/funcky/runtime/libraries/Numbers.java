package com.github.thanospapapetrou.funcky.runtime.libraries;

import java.net.URI;

import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.functors.ApplicableTwoArgumentArithmeticOperator;
import com.github.thanospapapetrou.funcky.runtime.literals.ApplicableFunction;
import com.github.thanospapapetrou.funcky.runtime.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.literals.Number;
import com.github.thanospapapetrou.funcky.runtime.literals.types.SimpleType;

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

	/**
	 * Construct and load a new numbers library.
	 * 
	 * @param engine
	 *            the engine loading this numbers library
	 * @throws ScriptException
	 *             if any errors occur while loading this numbers library
	 */
	public Numbers(final FunckyScriptEngine engine) throws ScriptException {
		super(engine);
		final SimpleType numberType = getSimpleType(NUMBERS, NUMBER);
		addDefinition(numberType);
		addDefinition(getNumber(Double.POSITIVE_INFINITY));
		addDefinition(getNumber(Double.NaN));
		addDefinition(PI, getNumber(Math.PI));
		addDefinition(E, getNumber(Math.E));
		addFunctionDefinition(IS_NAN, numberType, (SimpleType) engine.getReference(Booleans.class, Booleans.BOOLEAN).eval(), new ApplicableFunction() {
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws ScriptException {
				return (Literal) (Double.isNaN(((Number) argument.eval(context)).getValue()) ? engine.getReference(Booleans.class, Booleans.TRUE).eval() : engine.getReference(Booleans.class, Booleans.FALSE).eval());
			}
		});
		addFunctionDefinition(INTEGER, numberType, numberType, new ApplicableFunction() {
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws ScriptException {
				final double value = ((Number) argument.eval(context)).getValue();
				return new Number(engine, (Double.isInfinite(value) || Double.isNaN(value)) ? value : (int) value);
			}
		});
		addTwoArgumentArithmeticOperatorDefinition(ADD, numberType, new ApplicableTwoArgumentArithmeticOperator() {
			@Override
			public Literal apply(final Number term1, final Number term2, final ScriptContext context) {
				return new Number(engine, term1.getValue() + term2.getValue());
			}
		});
		addTwoArgumentArithmeticOperatorDefinition(SUBTRACT, numberType, new ApplicableTwoArgumentArithmeticOperator() {
			@Override
			public Literal apply(final Number minuend, final Number subtrahend, final ScriptContext context) {
				return new Number(engine, minuend.getValue() - subtrahend.getValue());
			}
		});
		addTwoArgumentArithmeticOperatorDefinition(MULTIPLY, numberType, new ApplicableTwoArgumentArithmeticOperator() {
			@Override
			public Literal apply(final Number factor1, final Number factor2, final ScriptContext context) {
				return new Number(engine, factor1.getValue() * factor2.getValue());
			}
		});
		addTwoArgumentArithmeticOperatorDefinition(DIVIDE, numberType, new ApplicableTwoArgumentArithmeticOperator() {
			@Override
			public Literal apply(final Number dividend, final Number divisor, final ScriptContext context) {
				return new Number(engine, dividend.getValue() / divisor.getValue());
			}
		});
	}

	private Number getNumber(final double value) {
		return new Number(engine, NUMBERS, value);
	}
}
