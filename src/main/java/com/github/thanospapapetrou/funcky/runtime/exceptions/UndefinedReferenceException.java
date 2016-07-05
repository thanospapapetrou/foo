package com.github.thanospapapetrou.funcky.runtime.exceptions;

import java.util.Objects;

import com.github.thanospapapetrou.funcky.FunckyException;
import com.github.thanospapapetrou.funcky.runtime.Reference;

/**
 * Exception thrown when encountering an undefined reference.
 * 
 * @author thanos
 */
public class UndefinedReferenceException extends FunckyException {
	private static final long serialVersionUID = 1L;
	private static final String UNDEFINED_REFERENCE = "Reference %1$s is undefined";
	private static final String NULL_UNDEFINED_REFERENCE = "Undefined reference must not be null";

	/**
	 * Construct a new undefined reference exception.
	 * 
	 * @param undefinedReference
	 *            the undefined reference
	 */
	public UndefinedReferenceException(final Reference undefinedReference) {
		super(String.format(UNDEFINED_REFERENCE, Objects.requireNonNull(undefinedReference, NULL_UNDEFINED_REFERENCE)), undefinedReference.getFileName(), undefinedReference.getLineNumber());
	}
}
