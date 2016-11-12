package com.github.thanospapapetrou.funcky.runtime.exceptions;

import java.util.Objects;

import com.github.thanospapapetrou.funcky.FunckyException;
import com.github.thanospapapetrou.funcky.runtime.Reference;

/**
 * Exception thrown when encountering an undefined symbol.
 * 
 * @author thanos
 */
public class UndefinedSymbolException extends FunckyException {
	private static final String NULL_REFERENCE = "Reference must not be null";
	private static final String UNDEFINED_REFERENCE = "Symbol %1$s is undefined in script %2$s";
	private static final long serialVersionUID = 1L;

	/**
	 * Construct a new undefined symbol exception.
	 * 
	 * @param reference
	 *            the reference with the undefined symbol
	 */
	public UndefinedSymbolException(final Reference reference) {
		super(String.format(UNDEFINED_REFERENCE, Objects.requireNonNull(reference, NULL_REFERENCE).getName(), reference.getNamespace()), reference.getScript(), reference.getLineNumber());
	}
}
