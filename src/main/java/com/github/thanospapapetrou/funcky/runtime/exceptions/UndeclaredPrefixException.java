package com.github.thanospapapetrou.funcky.runtime.exceptions;

import java.util.Objects;

import com.github.thanospapapetrou.funcky.FunckyException;
import com.github.thanospapapetrou.funcky.runtime.expressions.Reference;

/**
 * Exception thrown when encountering an undeclared prefix.
 * 
 * @author thanos
 */
public class UndeclaredPrefixException extends FunckyException {
	private static final String NULL_REFERENCE = "Reference must not be null";
	private static final String UNDECLARED_PREFIX = "Prefix %1$s is undeclared in script %2$s";
	private static final long serialVersionUID = 1L;

	/**
	 * Construct a new undeclared prefix exception.
	 * 
	 * @param reference
	 *            the reference with the undeclared prefix
	 */
	public UndeclaredPrefixException(final Reference reference) {
		super(String.format(UNDECLARED_PREFIX, Objects.requireNonNull(reference, NULL_REFERENCE).getPrefix(), reference.getScript()), reference.getScript(), reference.getLineNumber());
	}
}
