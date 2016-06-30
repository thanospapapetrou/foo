package com.github.thanospapapetrou.funcky;

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
	public FunckyException(final String message, final String fileName, int lineNumber) {
		super(message, fileName, lineNumber);
	}
}
