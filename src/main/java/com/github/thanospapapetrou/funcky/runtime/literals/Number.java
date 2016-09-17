package com.github.thanospapapetrou.funcky.runtime.literals;

import java.net.URI;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.Application;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.Reference;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.literals.types.SimpleType;

/**
 * Class representing a Funcky number.
 * 
 * @author thanos
 */
public class Number extends Literal {
	private static final String MINUS = "minus";

	private final double value;

	/**
	 * Construct a new number.
	 * 
	 * @param engine
	 *            the engine that generated this number
	 * @param script
	 *            the URI of the script from which this number was generated
	 * @param lineNumber
	 *            the number of the line from which this number was parsed or <code>0</code> if this number was not parsed (is builtin or generated at runtime)
	 * @param value
	 *            the value of this number
	 */
	public Number(final FunckyScriptEngine engine, final URI script, final int lineNumber, final double value) {
		super(engine, script, lineNumber);
		this.value = value;
	}

	/**
	 * Construct a new builtin number.
	 * 
	 * @param engine
	 *            the engine that generated this number
	 * @param script
	 *            the URI of the script from which this number was generated
	 * @param value
	 *            the value of this number
	 */
	public Number(final FunckyScriptEngine engine, final URI script, final double value) {
		this(engine, script, 0, value);
	}

	/**
	 * Construct a new number generated at runtime.
	 * 
	 * @param engine
	 *            the engine that constructed this number
	 * @param value
	 *            the value of this number
	 */
	public Number(final FunckyScriptEngine engine, final double value) {
		this(engine, FunckyScriptEngine.RUNTIME, value);
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof Number) && (value == ((Number) object).value);
	}

	@Override
	public SimpleType getType(final ScriptContext context) throws InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		super.getType(context);
		return engine.getPrelude().getNumber();
	}

	/**
	 * Get the value of this number.
	 * 
	 * @return the value of this number
	 */
	public double getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return Double.valueOf(value).hashCode();
	}

	@Override
	public Expression toExpression() {
		if ((value == Double.POSITIVE_INFINITY)) {
			return new Reference(engine, Double.toString(Double.POSITIVE_INFINITY).toLowerCase());
		} else if ((value == Double.NEGATIVE_INFINITY)) {
			return new Application(engine, new Reference(engine, MINUS), new Reference(engine, Double.toString(Double.POSITIVE_INFINITY).toLowerCase()));
		} else if (value == Double.NaN) {
			return new Reference(engine, Double.toString(Double.NaN));
		}
		return this;
	}

	@Override
	public String toString() {
		if ((value == Double.POSITIVE_INFINITY) || (value == Double.NEGATIVE_INFINITY) || (value == Double.NaN)) {
			return toExpression().toString();
		}
		return Double.toString(value);
	}
}
