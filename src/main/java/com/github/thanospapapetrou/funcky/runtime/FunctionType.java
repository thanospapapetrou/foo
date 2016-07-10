package com.github.thanospapapetrou.funcky.runtime;

import java.util.Objects;

/**
 * Class representing a Funcky function type.
 * 
 * @author thanos
 */
class FunctionType extends FunckyType {
	private final FunckyType domain;
	private final FunckyType range;

	FunctionType(final FunckyType domain, final FunckyType range) {
		this.domain = domain;
		this.range = range;
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

	public FunckyType getDomain() {
		return domain;
	}

	public FunckyType getRange() {
		return range;
	}

	@Override
	public Expression toExpression() {
		return new Application(new Application(Function.FUNCTION, domain), range);
	}

	@Override
	public String toString() {
		return toExpression().toString();
	}
}
