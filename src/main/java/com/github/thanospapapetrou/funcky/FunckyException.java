package com.github.thanospapapetrou.funcky;

import java.util.Objects;

import javax.script.ScriptException;

/**
 * Exception thrown when a parsing or runtime error occurs.
 * 
 * @author thanos
 */
public abstract class FunckyException extends ScriptException {
	private static final long serialVersionUID = 1L;

	/**
	 * Construct a new Funcky exception.
	 * 
	 * @param message
	 *            the message of the exception
	 * @param fileName
	 *            the name of the file in which the error occurred
	 * @param lineNumber
	 *            the line of the file in which the error occurred
	 */
	public FunckyException(final String message, final String fileName, final int lineNumber) {
		super(Objects.requireNonNull(message, "Message must not be null"), Objects.requireNonNull(fileName, "File name must not be null"), requirePositiveLineNumber(lineNumber));
	}

	private static int requirePositiveLineNumber(final int lineNumber) {
		if (lineNumber <= 0) {
			throw new IllegalArgumentException("Line number must be positive");
		}
		return lineNumber;
	}
}
