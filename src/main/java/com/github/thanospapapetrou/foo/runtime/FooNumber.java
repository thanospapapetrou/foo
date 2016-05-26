package com.github.thanospapapetrou.foo.runtime;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Class representing a Foo number.
 * 
 * @author thanos
 */
public class FooNumber extends Literal {
	private final BigDecimal value;

	/**
	 * Construct a new Foo number.
	 * 
	 * @param value
	 *            the value of this number
	 */
	public FooNumber(final BigDecimal value) {
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
