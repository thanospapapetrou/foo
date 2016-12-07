package com.github.thanospapapetrou.funcky.helper;

import java.util.regex.Pattern;

public enum TokenType {
	LEFT_PARENTHESIS("\\(", null),
	RIGHT_PARENTHESIS("\\)", null),
	SPACE(" ", null),
	SYMBOL("(?<symbol>\\w+)", "symbol"),
	VARIABLE("\\$(?<variable>\\d+)", "variable");

	private final Pattern pattern;
	private final String group;

	private TokenType(final String pattern, final String group) {
		this.pattern = Pattern.compile(pattern);
		this.group = group;
	}

	public Pattern getPattern() {
		return pattern;
	}
	
	public String getGroup() {
		return group;
	}
}
