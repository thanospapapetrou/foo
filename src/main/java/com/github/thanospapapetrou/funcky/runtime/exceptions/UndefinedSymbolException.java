package com.github.thanospapapetrou.funcky.runtime.exceptions;

import java.util.Objects;

import com.github.thanospapapetrou.funcky.FunckyException;
import com.github.thanospapapetrou.funcky.runtime.expressions.Reference;

/**
 * Exception thrown when encountering an undefined symbol.
 * 
 * @author thanos
 */
public class UndefinedSymbolException extends FunckyException {
	private static final String NULL_REFERENCE = "Reference must not be null";
	private static final String UNDEFINED_SYMBOL = "Symbol %1$s is undefined in script %2$s";
	private static final long serialVersionUID = 1L;

	/**
	 * Construct a new undefined symbol exception.
	 * 
	 * @param reference
	 *            the reference with the undefined symbol
	 */
	public UndefinedSymbolException(final Reference reference) {
		super(String.format(UNDEFINED_SYMBOL, Objects.requireNonNull(reference, NULL_REFERENCE).getName(), reference.getUri()), reference.getScript(), reference.getLineNumber());
	}
}
