package com.github.thanospapapetrou.funcky.runtime.exceptions;

import com.github.thanospapapetrou.funcky.FunckyException;
import com.github.thanospapapetrou.funcky.runtime.Expression;

public class InvalidFunctionException extends FunckyException {
	private static final long serialVersionUID = 1L;
	private static final String INVALID_FUNCTION = "%1$s is not a function";

	public InvalidFunctionException(final Expression function) {
		super(String.format(INVALID_FUNCTION, function), fileName, lineNumber);
	}
}
