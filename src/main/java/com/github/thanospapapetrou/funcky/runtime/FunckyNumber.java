package com.github.thanospapapetrou.funcky.runtime;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;

/**
 * Class representing a Funcky number.
 * 
 * @author thanos
 */
public class FunckyNumber extends Literal {
	/**
	 * Funcky number representing π.
	 */
	public static final FunckyNumber PI = new FunckyNumber(null, Math.PI);

	/**
	 * Funcky number representing e.
	 */
	public static final FunckyNumber E = new FunckyNumber(null, Math.E);

	/**
	 * Funcky number representing ∞.
	 */
	public static final FunckyNumber INFINITY = new FunckyNumber(null, Double.POSITIVE_INFINITY);

	/**
	 * Funcky number representing NaN.
	 */
	public static final FunckyNumber NAN = new FunckyNumber(null, Double.NaN);

	private final double value;

	/**
	 * Construct a new number.
	 * 
	 * @param engine
	 *            the engine that parsed this number
	 * @param value
	 *            the value of this number
	 */
	public FunckyNumber(final FunckyScriptEngine engine, final double value) {
		super(engine);
		this.value = value;
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof FunckyNumber) && (value == ((FunckyNumber) object).value);
	}

	@Override
	protected SimpleType getType() {
		return SimpleType.NUMBER;
	}

	@Override
	public int hashCode() {
		return Double.valueOf(value).hashCode();
	}

	@Override
	public String toString() {
		if (value == Double.POSITIVE_INFINITY) {
			return "infinity";
		} else if (value == Double.NEGATIVE_INFINITY) {
			return String.format("minus %1$s", INFINITY.toString());
		} else if (value == Double.NaN) {
			return "NaN";
		}
		return Double.toString(value);
	}

	double getValue() {
		return value;
	}
}
