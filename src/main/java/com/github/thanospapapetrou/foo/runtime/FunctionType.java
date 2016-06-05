package com.github.thanospapapetrou.foo.runtime;

import java.util.Objects;

/**
 * Class representing a Foo function type.
 * 
 * @author thanos
 */
public class FunctionType extends FooType {
	private static final String FUNCTION_TYPE = "function %1$s %2$s";

	private final FooType domain;
	private final FooType range;

	/**
	 * Construct a new function type.
	 * 
	 * @param domain
	 *            the function domain
	 * @param range
	 *            the function range
	 */
	public FunctionType(final FooType domain, final FooType range) {
		this.domain = Objects.requireNonNull(domain, "Domain must not be null");
		this.range = Objects.requireNonNull(range, "Range must not be null");
	}

	/**
	 * Get the domain.
	 * 
	 * @return the domain
	 */
	public FooType getDomain() {
		return domain;
	}

	/**
	 * Get the range.
	 * 
	 * @return the range
	 */
	public FooType getRange() {
		return range;
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof FunctionType) {
			final FunctionType functionType = (FunctionType) object;
			return domain.equals(functionType.domain) && range.equals(functionType.range);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(domain, range);
	}

	@Override
	public String toString() {
		return String.format(FUNCTION_TYPE, domain, range);
	}
}
