package com.github.thanospapapetrou.funcky.helper;

public class Symbol implements Expression {
	private final String value;

	public Symbol(final String value) {
		this.value = value;
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof Symbol) && value.equals(((Symbol) object).value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public String toString() {
		return value;
	}
}
