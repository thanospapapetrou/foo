package com.github.thanospapapetrou.funcky.runtime.libraries;

import java.util.Objects;

import com.github.thanospapapetrou.funcky.FunckyException;
import com.github.thanospapapetrou.funcky.runtime.Reference;

/**
 * Exception thrown when encountering an unknown built-in library.
 * 
 * @author thanos
 */
public class UnknownBuiltInLibraryException extends FunckyException {
	private static final String NULL_REFERENCE = "Reference must not be null";
	private static final String UNKNOWN_BUILT_IN_LIBRARY = "URI %1$s does not correspond to any built-in library";
	private static final long serialVersionUID = 1L;

	/**
	 * Construct a new unknown built-in library exception.
	 * 
	 * @param reference
	 *            the reference that refers to the unknown built-in library
	 */
	public UnknownBuiltInLibraryException(final Reference reference) {
		super(String.format(UNKNOWN_BUILT_IN_LIBRARY, Objects.requireNonNull(reference, NULL_REFERENCE).getUri()), reference.getScript(), reference.getLineNumber());
	}
}
