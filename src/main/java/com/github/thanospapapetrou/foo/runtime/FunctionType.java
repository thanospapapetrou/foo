package com.github.thanospapapetrou.foo.runtime;

import java.util.Objects;

public class FunctionType extends FooType {
	private final FooType domain;
	private final FooType range;

	public FunctionType(final FooType domain, final FooType range) {
		this.domain = Objects.requireNonNull(domain, "Domain must not be null");
		this.range = Objects.requireNonNull(range, "Range must not be null");
	}
}
