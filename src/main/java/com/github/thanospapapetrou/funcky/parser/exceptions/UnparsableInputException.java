package com.github.thanospapapetrou.funcky.parser.exceptions;

import java.net.URI;

import com.github.thanospapapetrou.funcky.FunckyException;

/**
 * Exception thrown by parser when encountering unparsable input.
 * 
 * @author thanos
 */
public class UnparsableInputException extends FunckyException {
	private static final String UNPARSABLE_INPUT = "Unparsable input %1$s";
	private static final long serialVersionUID = 1L;

	/**
	 * Construct a new unparsable input exception.
	 * 
	 * @param unparsableInput
	 *            the unparsable input
	 * @param script
	 *            the URI of the script in which the unparsable input was encountered
	 * @param line
	 *            the line of the script in which the unparsable input was encountered
	 */
	public UnparsableInputException(final char unparsableInput, final URI script, final int line) {
		super(String.format(UNPARSABLE_INPUT, unparsableInput), script, line);
	}
}
