package com.github.thanospapapetrou.funcky.runtime.exceptions;

import com.github.thanospapapetrou.funcky.FunckyException;
import com.github.thanospapapetrou.funcky.runtime.Reference;

public class UndefinedReferenceException extends FunckyException {
	private static final long serialVersionUID = 1L;
	private static final String UNDEFINED_REFERENCE = "Reference %1$s is undefined";

	public UndefinedReferenceException(final Reference reference) {
		super(String.format(UNDEFINED_REFERENCE, reference), fileName, lineNumber);
	}
}
