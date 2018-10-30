package com.github.thanospapapetrou.funcky.script.expression.exception;

import com.github.thanospapapetrou.funcky.FunckyEngine;
import com.github.thanospapapetrou.funcky.FunckyException;

import java.net.URI;

/**
 * Abstract exception thrown by {@link FunckyEngine} if an error occurs while checking the type of
 * an expression.
 * 
 * @author thanos
 */
public abstract class TypeCheckException extends FunckyException {
    private static final long serialVersionUID = 0L;

    /**
     * Construct a new type check exception.
     * 
     * @param message
     *            the error message of this type check exception
     * @param script
     *            the URI of the script in which the error occurred
     * @param line
     *            the line at which the error occurred (starting from <code>1</code>)
     * @param column
     *            the column at which the error occurred (starting from <code>1</code>)
     */
    protected TypeCheckException(final String message, final URI script, final int line,
            final int column) {
        super(message, script, line, column);
    }
}
