package com.github.thanospapapetrou.funcky.parser.exceptions;

import com.github.thanospapapetrou.funcky.FunckyException;

/**
 * Exception thrown by parser when encountering unparsable input.
 * 
 * @author thanos
 */
public class UnparsableInputException extends FunckyException {
	private static final long serialVersionUID = 1L;
	private static final String UNPARSABLE_INPUT = "Unparsable input %1$s";

	/**
	 * Construct a new unparsable input exception.
	 * 
	 * @param unparsableInput
	 *            the unparsable input
	 * @param fileName
	 *            the name of the file in which the unparsable input was encountered
	 * @param lineNumber
	 *            the line of the file in which the unparsable input was encountered
	 */
	public UnparsableInputException(final String unparsableInput, final String fileName, final int lineNumber) {
		super(String.format(UNPARSABLE_INPUT, unparsableInput), fileName, lineNumber);
	}
}
