package com.github.thanospapapetrou.foo.runtime;

import com.github.thanospapapetrou.foo.FooScriptEngine;

/**
 * Class representing a Foo number.
 * 
 * @author thanos
 */
public class FooNumber extends Literal {
	/**
	 * Foo number representing π.
	 */
	public static final FooNumber PI = new FooNumber(null, Math.PI);

	/**
	 * Foo number representing e.
	 */
	public static final FooNumber E = new FooNumber(null, Math.E);

	/**
	 * Foo number representing +∞.
	 */
	public static final FooNumber POSITIVE_INFINITY = new FooNumber(null, Double.POSITIVE_INFINITY);

	/**
	 * Foo number representing -∞.
	 */
	public static final FooNumber NEGATIVE_INFINITY = new FooNumber(null, Double.NEGATIVE_INFINITY);

	/**
	 * Foo number representing NaN.
	 */
	public static final FooNumber NaN = new FooNumber(null, Double.NaN);

	private final double value;

	/**
	 * Construct a new Foo number.
	 * 
	 * @param engine
	 *            the Foo script engine that created this number
	 * @param value
	 *            the value of this number
	 */
	public FooNumber(final FooScriptEngine engine, final double value) {
		super(engine);
		this.value = value;
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof FooNumber) && (value == ((FooNumber) object).value);
	}

	@Override
	public int hashCode() {
		return Double.valueOf(value).hashCode();
	}

	@Override
	public String toString() {
		return Double.toString(value);
	}

	double getValue() {
		return value;
	}

	@Override
	protected SimpleType getType() {
		return SimpleType.NUMBER;
	}
}
