package com.github.thanospapapetrou.funcky.parser.exceptions;

import java.net.URI;
import java.util.Objects;

import com.github.thanospapapetrou.funcky.FunckyException;

/**
 * Exception thrown by parser when encountering an invalid character literal.
 * 
 * @author thanos
 */
public class InvalidCharacterLiteralException extends FunckyException {
	private static final String INVALID_CHARACTER_LITERAL = "Invalid character literal %1$s";
	private static final String NULL_CHARACTER_LITERAL = "Character literal must not be null";
	private static final long serialVersionUID = 1L;

	/**
	 * Construct a new invalid character literal exception.
	 * 
	 * @param invalidCharacterLiteral
	 *            the invalid character literal
	 * @param script
	 *            the URI of the script in which the invalid character literal was encountered
	 * @param lineNumber
	 *            the line of the script in which the invalid character literal was encountered
	 */
	public InvalidCharacterLiteralException(final String invalidCharacterLiteral, final URI script, final int lineNumber) {
		super(String.format(INVALID_CHARACTER_LITERAL, Objects.requireNonNull(invalidCharacterLiteral, NULL_CHARACTER_LITERAL)), script, lineNumber);
	}
}
