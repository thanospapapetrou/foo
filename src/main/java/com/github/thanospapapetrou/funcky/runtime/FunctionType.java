package com.github.thanospapapetrou.funcky.runtime;

import java.util.Objects;

/**
 * Class representing a Funcky function type.
 * 
 * @author thanos
 */
public class FunctionType extends FunckyType {
	private final FunckyType domain;
	private final FunckyType range;

	FunctionType(final FunckyType domain, final FunckyType range) {
		this.domain = Objects.requireNonNull(domain, "Domain must not be null");
		this.range = Objects.requireNonNull(range, "Range must not be null");
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
