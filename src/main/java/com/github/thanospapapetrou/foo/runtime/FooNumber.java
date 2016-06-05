package com.github.thanospapapetrou.foo.runtime;

import java.math.BigDecimal;
import java.util.Objects;

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
	public static final FooNumber PI = new FooNumber(null, BigDecimal.valueOf(Math.PI));

	/**
	 * Foo number representing e.
	 */
	public static final FooNumber E = new FooNumber(null, BigDecimal.valueOf(Math.E));

	/**
	 * Foo number representing +∞.
	 */
	public static final FooNumber POSITIVE_INFINITY = new FooNumber(null, BigDecimal.valueOf(Double.POSITIVE_INFINITY));

	/**
	 * Foo number representing -∞.
	 */
	public static final FooNumber NEGATIVE_INFINITY = new FooNumber(null, BigDecimal.valueOf(Double.NEGATIVE_INFINITY));

	/**
	 * Foo number representing NaN.
	 */
	public static final FooNumber NaN = new FooNumber(null, BigDecimal.valueOf(Double.NaN));

	private final BigDecimal value;

	/**
	 * Construct a new Foo number.
	 * 
	 * @param engine
	 *            the Foo script engine that created this number
	 * @param value
	 *            the value of this number
	 */
	public FooNumber(final FooScriptEngine engine, final BigDecimal value) {
		super(engine);
		this.value = Objects.requireNonNull(value, "Value must not be null");
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof FooNumber) && value.equals(((FooNumber) object).value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	protected SimpleType getType() {
		return SimpleType.NUMBER;
	}
}
