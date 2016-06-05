package com.github.thanospapapetrou.foo.runtime;

import java.util.Objects;

public abstract class Function extends Literal {
	private final FooType domain;
	private final FooType range;

	private Function(final FooType domain, final FooType range) {
		super(null);
		this.domain = Objects.requireNonNull(domain, "Domain must not be null");
		this.range = Objects.requireNonNull(range, "Range must not be null");
	}

	public abstract Literal apply(final Expression expression);

	@Override
	protected FooType getType() {
		return new FunctionType(domain, range);
	}
}
