package com.github.thanospapapetrou.funcky.runtime;

import java.util.Objects;

public class TypeVariable extends FunckyType {
	private static final String TYPE_VARIABLE = "<%1$s>";
	private static final String NULL_NAME = "Name must not be null";
	private static final String EMPTY_NAME = "Name must not be empty";

	private final String name;

	public TypeVariable(final String name) {
		super();
		this.name = Objects.requireNonNull(name, NULL_NAME);
		if (name.isEmpty()) {
			throw new IllegalArgumentException(EMPTY_NAME);
		}
	}

	@Override
	public String toString() {
		return String.format(TYPE_VARIABLE, name);
	}

	@Override
	Expression toExpression() {
		return this;
	}
}
