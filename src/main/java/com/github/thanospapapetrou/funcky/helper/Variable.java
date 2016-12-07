package com.github.thanospapapetrou.funcky.helper;

public class Variable implements Expression {
	private final int value;

	public Variable(final int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof Variable) && (value == ((Variable) object).value);
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public String toString() {
		return String.format("$%1$d", value);
	}
}
