package com.github.thanospapapetrou.funcky.helper;

public class Token {
	private final TokenType type;
	private final String value;

	public Token(final TokenType type, final String value) {
		this.type = type;
		this.value = value;
	}

	public TokenType getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.format("%1$s %2$s", type, value);
	}
}
