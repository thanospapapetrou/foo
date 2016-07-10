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
	public static final FunckyNumber PI = new FunckyNumber(Math.PI);

	/**
	 * Funcky number representing e.
	 */
	public static final FunckyNumber E = new FunckyNumber(Math.E);

	/**
	 * Funcky number representing ∞.
	 */
	public static final FunckyNumber INFINITY = new FunckyNumber(Double.POSITIVE_INFINITY);

	/**
	 * Funcky number representing NaN.
	 */
	public static final FunckyNumber NAN = new FunckyNumber(Double.NaN);

	private static final Reference MINUS = new Reference("minus");

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
		super(requireNonNullEngine(engine), requireValidFileName(fileName), requirePositiveLineNumber(lineNumber));
		this.value = value;
	}

	FunckyNumber(final double value) {
		super(null, null, 0);
		this.value = value;
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof FunckyNumber) && (value == ((FunckyNumber) object).value);
	}

	@Override
	public SimpleType getType() {
		return SimpleType.NUMBER;
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
	public String toString() {
		if ((value == Double.POSITIVE_INFINITY) || (value == Double.NEGATIVE_INFINITY) || (value == Double.NaN)) {
			return toExpression().toString();
		}
		return Double.toString(value);
	}

	@Override
	public Expression toExpression() {
		if ((value == Double.POSITIVE_INFINITY)) {
			return new Reference(Double.toString(value).toLowerCase());
		} else if ((value == Double.NEGATIVE_INFINITY)) {
			return new Application(MINUS, INFINITY.toExpression());
		} else if (value == Double.NaN) {
			return new Reference(Double.toString(value));
		}
		return this;
	}
}
