package com.github.thanospapapetrou.funcky.parser.exceptions;

import java.net.URI;
import java.util.Objects;

import com.github.thanospapapetrou.funcky.FunckyException;
import com.github.thanospapapetrou.funcky.parser.Token;

/**
 * Exception thrown by parser when encountering an unexpected token.
 * 
 * @author thanos
 */
public class UnexpectedTokenException extends FunckyException {
	private static final String COMMA = ", ";
	private static final String EMPTY_EXPECTED_TOKENS = "Expected tokens must not be empty";
	private static final String NULL_EXPECTED_TOKEN = "Expected token %1$d must not be null";
	private static final String NULL_EXPECTED_TOKENS = "Expected tokens must not be null";
	private static final String NULL_UNEXPECTED_TOKEN = "Unexpected token must not be null";
	private static final String OR = " or ";
	private static final String UNEXPECTED_TOKEN = "Unexpected %1$s, expected %2$s";
	private static final long serialVersionUID = 1L;

	private static String or(final Token... tokens) {
		final StringBuilder or = new StringBuilder(tokens[0].getName());
		for (int i = 1; i < tokens.length; i++) {
			or.append((i == tokens.length - 1) ? OR : COMMA).append(tokens[i].getName());
		}
		return or.toString();
	}

	private static final Token[] requireValidExpectedTokens(final Token[] expectedTokens) {
		if (Objects.requireNonNull(expectedTokens, NULL_EXPECTED_TOKENS).length == 0) {
			throw new IllegalArgumentException(EMPTY_EXPECTED_TOKENS);
		}
		for (int i = 0; i < expectedTokens.length; i++) {
			Objects.requireNonNull(expectedTokens[i], String.format(NULL_EXPECTED_TOKEN, i));
		}
		return expectedTokens;
	}

	/**
	 * Construct a new unexpected token exception.
	 * 
	 * @param unexpectedToken
	 *            the unexpected token
	 * @param script
	 *            the URI of the script in which the unexpected token was encountered
	 * @param line
	 *            the line of the script in which the unexpected token was encountered
	 * @param expectedTokens
	 *            the tokens normally expected
	 */
	public UnexpectedTokenException(final Token unexpectedToken, final URI script, final int line, final Token... expectedTokens) {
		super(String.format(UNEXPECTED_TOKEN, Objects.requireNonNull(unexpectedToken, NULL_UNEXPECTED_TOKEN).getName(), or(requireValidExpectedTokens(expectedTokens))), script, line);
	}
}
