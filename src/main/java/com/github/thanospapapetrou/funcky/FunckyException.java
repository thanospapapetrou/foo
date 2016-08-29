package com.github.thanospapapetrou.funcky;

import java.net.URI;
import java.util.Objects;

import javax.script.ScriptException;

/**
 * Exception thrown when a parsing or runtime error occurs.
 * 
 * @author thanos
 */
public abstract class FunckyException extends ScriptException {
	private static final long serialVersionUID = 1L;
	private static final String EMPTY_MESSAGE = "Message must not be empty";
	private static final String NEGATIVE_LINE_NUMBER = "Line number must be non-negative";
	private static final String NULL_MESSAGE = "Message must not be null";
	private static final String NULL_SCRIPT = "Script must not be null";

	/**
	 * Construct a new Funcky exception.
	 * 
	 * @param message
	 *            the message of the exception
	 * @param script
	 *            the URI of the script (or library) in which the error occurred
	 * @param lineNumber
	 *            the number of the line in which the error occurred or <code>0</code> if this error occured at runtime
	 */
	protected FunckyException(final String message, final URI script, final int lineNumber) {
		super(Objects.requireNonNull(message, NULL_MESSAGE), Objects.requireNonNull(script, NULL_SCRIPT).toString(), lineNumber);
		if (message.isEmpty()) {
			throw new IllegalArgumentException(EMPTY_MESSAGE);
		}
		if (lineNumber < 0) {
			throw new IllegalArgumentException(NEGATIVE_LINE_NUMBER);
		}
	}
}
