package com.github.thanospapapetrou.funcky;

import java.util.Objects;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.runtime.AbstractSyntaxTreeNode;

/**
 * Exception thrown when a parsing or runtime error occurs.
 * 
 * @author thanos
 */
public abstract class FunckyException extends ScriptException {
	private static final long serialVersionUID = 1L;
	private static final String NULL_MESSAGE = "Message must not be null";
	private static final String EMPTY_MESSAGE = "Message must not be empty";
	private static final String NULL_FILE_NAME = "File name must not be null";
	private static final String EMPTY_FILE_NAME = "File name must not be empty";

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
		super(requireValidString(message, NULL_MESSAGE, EMPTY_MESSAGE), requireValidString(fileName, NULL_FILE_NAME, EMPTY_FILE_NAME), AbstractSyntaxTreeNode.requirePositiveLineNumber(lineNumber));
	}

	private static final String requireValidString(final String string, final String nullMessage, final String emptyMessage) {
		Objects.requireNonNull(string, nullMessage);
		if (string.isEmpty()) {
			throw new IllegalArgumentException(emptyMessage);
		}
		return string;
	}
}
