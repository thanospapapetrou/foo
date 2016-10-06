package com.github.thanospapapetrou.funcky.parser.exceptions;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.github.thanospapapetrou.funcky.FunckyException;
import com.github.thanospapapetrou.funcky.parser.Parser;

/**
 * Exception thrown by parser when encountering an unexpected token.
 * 
 * @author thanos
 */
public class UnexpectedTokenException extends FunckyException {
	private static final String COMMA = ", ";
	private static final String EMPTY_EXPECTED_TOKENS = "Expected tokens must not be empty";
	private static final String INVALID_TOKEN = "Invalid token %1$d";
	private static final String NULL_EXPECTED_TOKENS = "Expected tokens must not be null";
	private static final String OR = " or ";
	private static final Map<Integer, String> TOKEN_NAMES = new HashMap<>();
	private static final String UNEXPECTED_TOKEN = "Unexpected %1$s, expected %2$s";
	private static final long serialVersionUID = 1L;

	static {
		TOKEN_NAMES.put(Parser.CHARACTER, "character");
		TOKEN_NAMES.put(Parser.COLON, "colon");
		TOKEN_NAMES.put(Parser.COMMA, "comma");
		TOKEN_NAMES.put(Parser.COMMENT, "comment");
		TOKEN_NAMES.put(Parser.DOLLAR, "dollar");
		TOKEN_NAMES.put(Parser.EOF, "end of input");
		TOKEN_NAMES.put(Parser.EOL, "end of line");
		TOKEN_NAMES.put(Parser.EQUALS, "equals");
		TOKEN_NAMES.put(Parser.LEFT_ANGLE_BRACKET, "left angle bracket");
		TOKEN_NAMES.put(Parser.LEFT_CURLY_BRACKET, "left curly bracket");
		TOKEN_NAMES.put(Parser.LEFT_PARENTHESIS, "left parenthesis");
		TOKEN_NAMES.put(Parser.LEFT_SQUARE_BRACKET, "left square bracket");
		TOKEN_NAMES.put(Parser.NUMBER, "number");
		TOKEN_NAMES.put(Parser.RIGHT_ANGLE_BRACKET, "right angle bracket");
		TOKEN_NAMES.put(Parser.RIGHT_CURLY_BRACKET, "right curly bracket");
		TOKEN_NAMES.put(Parser.RIGHT_PARENTHESIS, "right parenthesis");
		TOKEN_NAMES.put(Parser.RIGHT_SQUARE_BRACKET, "right square bracket");
		TOKEN_NAMES.put(Parser.STRING, "string");
		TOKEN_NAMES.put(Parser.SYMBOL, "symbol");
	}

	private static String or(final int... tokens) {
		final StringBuilder or = new StringBuilder(TOKEN_NAMES.get(tokens[0]));
		for (int i = 1; i < tokens.length; i++) {
			or.append((i == tokens.length - 1) ? OR : COMMA).append(TOKEN_NAMES.get(tokens[i]));
		}
		return or.toString();
	}

	private static final int[] requireValidExpectedTokens(final int[] expectedTokens) {
		if (Objects.requireNonNull(expectedTokens, NULL_EXPECTED_TOKENS).length == 0) {
			throw new IllegalArgumentException(EMPTY_EXPECTED_TOKENS);
		}
		for (final int expectedToken : expectedTokens) {
			requireValidToken(expectedToken);
		}
		return expectedTokens;
	}

	private static final int requireValidToken(final int token) {
		if (!TOKEN_NAMES.containsKey(token)) {
			throw new IllegalArgumentException(String.format(INVALID_TOKEN, token));
		}
		return token;
	}

	/**
	 * Construct a new unexpected token exception.
	 * 
	 * @param unexpectedToken
	 *            the unexpected token
	 * @param script
	 *            the URI of the script in which the unexpected token was encountered
	 * @param lineNumber
	 *            the line of the script in which the unexpected token was encountered
	 * @param expectedTokens
	 *            the tokens normally expected
	 */
	public UnexpectedTokenException(final int unexpectedToken, final URI script, final int lineNumber, final int... expectedTokens) {
		super(String.format(UNEXPECTED_TOKEN, TOKEN_NAMES.get(requireValidToken(unexpectedToken)), or(requireValidExpectedTokens(expectedTokens))), script, lineNumber);
	}
}
