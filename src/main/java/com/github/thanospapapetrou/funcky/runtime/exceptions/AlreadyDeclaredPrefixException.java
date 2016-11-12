package com.github.thanospapapetrou.funcky.runtime.exceptions;

import java.util.Objects;

import com.github.thanospapapetrou.funcky.FunckyException;
import com.github.thanospapapetrou.funcky.runtime.Import;

/**
 * Exception thrown when encountering an import for an already declared prefix.
 * 
 * @author thanos
 */
public class AlreadyDeclaredPrefixException extends FunckyException {
	private static final String ALREADY_DECLARED_PREFIX = "Prefix %1$s is already declared in script %2$s";
	private static final String NULL_IMPORT = "Import must not be null";
	private static final long serialVersionUID = 1L;

	/**
	 * Construct a new already declared prefix exception.
	 * 
	 * @param impord
	 *            the import of the already declared prefix
	 */
	public AlreadyDeclaredPrefixException(final Import impord) {
		super(String.format(ALREADY_DECLARED_PREFIX, Objects.requireNonNull(impord, NULL_IMPORT).getPrefix(), impord.getScript()), impord.getScript(), impord.getLineNumber());
	}
}
