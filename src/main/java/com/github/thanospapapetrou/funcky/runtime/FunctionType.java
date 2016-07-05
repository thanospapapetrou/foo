package com.github.thanospapapetrou.funcky.runtime;

import java.util.Objects;

/**
 * Class representing a Funcky function type.
 * 
 * @author thanos
 */
public class FunctionType extends FunckyType {
	private static final String NULL_DOMAIN = "Domain must not be null";
	private static final String NULL_RANGE = "Range must not be null";

	private final FunckyType domain;
	private final FunckyType range;

	FunctionType(final FunckyType domain, final FunckyType range) {
		this.domain = Objects.requireNonNull(domain, NULL_DOMAIN);
		this.range = Objects.requireNonNull(range, NULL_RANGE);
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
		return new Application(new Application(Function.FUNCTION, domain), range).toString();
	}

	FunckyType getDomain() {
		return domain;
	}

	FunckyType getRange() {
		return range;
	}
}
