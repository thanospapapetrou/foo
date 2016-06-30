package com.github.thanospapapetrou.funcky.parser;

import java.util.HashMap;
import java.util.Map;

import com.github.thanospapapetrou.funcky.FunckyException;

/**
 * Expression thrown by parser when encountering an unexpected token.
 * 
 * @author thanos
 */
public class UnexpectedTokenException extends FunckyException {
	private static final long serialVersionUID = 1L;
	private static final String UNEXPECTED_TOKEN = "Unexpected %1$s, expected %2$s";
	private static final String COMMA = ", ";
	private static final String OR = " or ";
	private static final Map<Integer, String> TOKEN_NAMES = new HashMap<>();
	static {
		TOKEN_NAMES.put(Parser.EQUALS, "equals");
		TOKEN_NAMES.put(Parser.LEFT_PARENTHESIS, "left parenthesis");
		TOKEN_NAMES.put(Parser.RIGHT_PARENTHESIS, "right parenthesis");
		TOKEN_NAMES.put(Parser.SYMBOL, "symbol");
		TOKEN_NAMES.put(Parser.NUMBER, "number");
		TOKEN_NAMES.put(Parser.CHARACTER, "character");
		TOKEN_NAMES.put(Parser.STRING, "string");
		TOKEN_NAMES.put(Parser.EOL, "end of line");
		TOKEN_NAMES.put(Parser.EOF, "end of input");
	}

	/**
	 * Construct a new unexpected token exception.
	 * 
	 * @param unexpectedToken
	 *            the unexpected token
	 * @param fileName
	 *            the name of the file in which the unexpected token was encountered
	 * @param lineNumber
	 *            the line of the file in which the unexpected token was encountered
	 * @param expectedTokens
	 *            the tokens normally expected
	 */
	public UnexpectedTokenException(final int unexpectedToken, final String fileName, final int lineNumber, final int... expectedTokens) {
		super(String.format(UNEXPECTED_TOKEN, TOKEN_NAMES.get(unexpectedToken), or(expectedTokens)), fileName, lineNumber);
	}

	private static String or(final int... tokens) {
		final StringBuilder or = new StringBuilder(TOKEN_NAMES.get(tokens[0]));
		for (int i = 1; i < tokens.length; i++) {
			or.append((i == tokens.length - 1) ? OR : COMMA).append(TOKEN_NAMES.get(tokens[i]));
		}
		return or.toString();
	}
}
