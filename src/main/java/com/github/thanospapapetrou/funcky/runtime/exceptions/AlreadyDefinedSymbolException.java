package com.github.thanospapapetrou.funcky.runtime.exceptions;

import java.util.Objects;

import com.github.thanospapapetrou.funcky.FunckyException;
import com.github.thanospapapetrou.funcky.runtime.Definition;

/**
 * Exception thrown when encountering a definition for an already defined symbol.
 * 
 * @author thanos
 */
public class AlreadyDefinedSymbolException extends FunckyException {
	private static final String ALREADY_DEFINED_SYMBOL = "Symbol %1$s is already defined in this context";
	private static final String NULL_DEFINITION = "Definition must not be null";
	private static final long serialVersionUID = 1L;

	/**
	 * Construct a new already defined symbol exception.
	 * 
	 * @param definition
	 *            the definition of the already defined symbol
	 */
	public AlreadyDefinedSymbolException(final Definition definition) {
		super(String.format(ALREADY_DEFINED_SYMBOL, Objects.requireNonNull(definition, NULL_DEFINITION).getName()), definition.getScript(), definition.getLineNumber());
	}
}
