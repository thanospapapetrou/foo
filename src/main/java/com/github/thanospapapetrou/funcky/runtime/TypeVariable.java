package com.github.thanospapapetrou.funcky.runtime;

import java.util.Objects;

public class TypeVariable extends FunckyType {
	private static final String TYPE_VARIABLE = "<%1$s>";

	private final String name;

	public TypeVariable(final String name) {
		super();
		this.name = Objects.requireNonNull(name, "Name must not be null");
	}

	@Override
	public String toString() {
		return String.format(TYPE_VARIABLE, name);
	}
}
