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
	public static final FunckyNumber PI = new FunckyNumber(null, null, 0, Math.PI);

	/**
	 * Funcky number representing e.
	 */
	public static final FunckyNumber E = new FunckyNumber(null, null, 0, Math.E);

	/**
	 * Funcky number representing ∞.
	 */
	public static final FunckyNumber INFINITY = new FunckyNumber(null, null, 0, Double.POSITIVE_INFINITY);

	/**
	 * Funcky number representing NaN.
	 */
	public static final FunckyNumber NAN = new FunckyNumber(null, null, 0, Double.NaN);

	private static final String MINUS = "minus %1$s";

	private final double value;

	/**
	 * Construct a new number.
	 * 
	 * @param engine
	 *            the engine that parsed this number or <code>null</code> if this number was not parsed by any engine
	 * @param fileName
	 *            the name of the file from which this number was parsed or <code>null</code> if this number was not parsed from any file
	 * @param lineNumber
	 *            the number of the line from which this number was parsed or <code>0</code> if this number was not parsed from any line
	 * @param value
	 *            the value of this number
	 */
	public FunckyNumber(final FunckyScriptEngine engine, final String fileName, final int lineNumber, final double value) {
		super(engine, fileName, lineNumber);
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
			return Double.toString(Double.POSITIVE_INFINITY).toLowerCase();
		} else if (value == Double.NEGATIVE_INFINITY) {
			return String.format(MINUS, INFINITY);
		}
		return Double.toString(value);
	}

	double getValue() {
		return value;
	}
}
