package com.github.thanospapapetrou.funcky.runtime.libraries.exceptions;

import java.net.URI;

import com.github.thanospapapetrou.funcky.FunckyException;

public class UnknownLibraryException extends FunckyException {
	private static final long serialVersionUID = 1L;

	protected UnknownLibraryException(final String message, final URI script, final int line) {
		super(message, script, line);
	}
}
