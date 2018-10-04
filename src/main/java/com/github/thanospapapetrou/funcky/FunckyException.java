package com.github.thanospapapetrou.funcky;

import java.net.URI;

import javax.script.ScriptException;

/**
 * Exception thrown when a parsing or runtime error occurs.
 * 
 * @author thanos
 */
public abstract class FunckyException extends ScriptException {
    private static final long serialVersionUID = 1L;

    /**
     * Construct a new Funcky exception.
     * 
     * @param message
     *            the message of the exception
     * @param script
     *            the URI of the script (or library) in which the error occurred
     * @param line
     *            the line in which the error occurred or <code>-1</code> if this error occurred at
     *            runtime
     * @param column
     *            the column in which the error occured or <code>-1</code> if this error occured at
     *            runtime
     */
    protected FunckyException(final String message, final URI script, final int line,
            final int column) {
        super(message, script.toString(), line, column);
    }
}
