package com.github.thanospapapetrou.funcky.helper;

import java.util.List;

public class ParseState {
	private final List<Token> tokens;
	private final Expression expression;

	public ParseState(final List<Token> tokens) {
		this.tokens = tokens;
		expression = null;
	}

	public ParseState(final List<Token> tokens, final Expression expression) {
		this.tokens = tokens;
		this.expression = expression;
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public String toString() {
		return String.format("%1$s %2$s", expression, tokens);
	}
}
