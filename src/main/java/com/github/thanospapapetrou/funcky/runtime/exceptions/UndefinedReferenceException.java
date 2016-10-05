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
	private static final String NULL_REFERENCE = "Reference must not be null";
	private static final String UNDEFINED_REFERENCE = "Reference %1$s is undefined";
	private static final long serialVersionUID = 1L;

	/**
	 * Construct a new undefined reference exception.
	 * 
	 * @param reference
	 *            the undefined reference
	 */
	public UndefinedReferenceException(final Reference reference) {
		super(String.format(UNDEFINED_REFERENCE, Objects.requireNonNull(reference, NULL_REFERENCE)), reference.getScript(), reference.getLineNumber());
	}
}
