package com.github.thanospapapetrou.funcky.runtime.exceptions;

import java.util.Objects;

import com.github.thanospapapetrou.funcky.FunckyException;
import com.github.thanospapapetrou.funcky.runtime.Expression;

/**
 * Exception thrown when encountering an invalid function.
 * 
 * @author thanos
 */
public class InvalidFunctionException extends FunckyException {
	private static final String INVALID_FUNCTION = "%1$s is not a function";
	private static final String NULL_INVALID_FUNCTION = "Invalid function must not be null";
	private static final long serialVersionUID = 1L;

	/**
	 * Construct a new invalid function exception.
	 * 
	 * @param invalidFunction
	 *            the invalid function expression
	 */
	public InvalidFunctionException(final Expression invalidFunction) {
		super(String.format(INVALID_FUNCTION, Objects.requireNonNull(invalidFunction, NULL_INVALID_FUNCTION)), invalidFunction.getScript(), invalidFunction.getLineNumber());
	}
}
