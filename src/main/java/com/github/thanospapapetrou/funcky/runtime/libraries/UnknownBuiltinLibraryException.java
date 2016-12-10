package com.github.thanospapapetrou.funcky.runtime.libraries;

import java.util.Objects;

import com.github.thanospapapetrou.funcky.FunckyException;
import com.github.thanospapapetrou.funcky.runtime.Reference;

/**
 * Exception thrown when encountering an unknown builtin library.
 * 
 * @author thanos
 */
public class UnknownBuiltinLibraryException extends FunckyException {
	private static final String NULL_REFERENCE = "Reference must not be null";
	private static final String UNKNOWN_LIBRARY = "URI %1$s does not correspond to any builtin library";
	private static final long serialVersionUID = 1L;

	/**
	 * Construct a new unknown builtin library exception.
	 * 
	 * @param reference
	 *            the reference that refers to the unknown builtin library
	 */
	public UnknownBuiltinLibraryException(final Reference reference) {
		super(String.format(UNKNOWN_LIBRARY, Objects.requireNonNull(reference, NULL_REFERENCE).getUri()), reference.getScript(), reference.getLineNumber());
	}
}
