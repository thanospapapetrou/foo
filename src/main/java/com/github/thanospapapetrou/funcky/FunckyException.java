package com.github.thanospapapetrou.funcky;

import javax.script.ScriptException;

public abstract class FunckyException extends ScriptException {
	private static final long serialVersionUID = 1L;

	public FunckyException(final String message, final String fileName, int lineNumber) {
		super(message, fileName, lineNumber);
	}
}
