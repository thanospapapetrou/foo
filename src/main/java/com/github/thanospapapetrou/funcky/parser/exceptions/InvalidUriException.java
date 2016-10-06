package com.github.thanospapapetrou.funcky.parser.exceptions;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import com.github.thanospapapetrou.funcky.FunckyException;

/**
 * Exception thrown by parser when encountering an invalid URI.
 * 
 * @author thanos
 */
public class InvalidUriException extends FunckyException {
	private static final String INVALID_URI = "Invalid URI %1$s";
	private static final String NULL_EXCEPTION = "Exception must not be null";
	private static final long serialVersionUID = 1L;

	/**
	 * Construct a new invalid URI exception.
	 * 
	 * @param exception
	 *            the <code>URISyntaxException</code> that caused this invalid URI exception
	 * @param script
	 *            the URI of the script in which the invalid URI was encountered
	 * @param lineNumber
	 *            the line of the script in which the invalid URI was encountered
	 */
	public InvalidUriException(final URISyntaxException exception, final URI script, final int lineNumber) {
		super(String.format(INVALID_URI, Objects.requireNonNull(exception, NULL_EXCEPTION).getInput()), script, lineNumber);
	}
}
