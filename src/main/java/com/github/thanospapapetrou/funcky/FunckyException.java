package com.github.thanospapapetrou.funcky;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;

import javax.script.ScriptException;

/**
 * Abstract exception thrown by {@link FunckyEngine} if an error occurs.
 * 
 * @author thanos
 */
public abstract class FunckyException extends ScriptException {
    private static final String NON_POSITIVE_COLUMN = "Column must be positive";
    private static final String NON_POSITIVE_LINE = "Line must be positive";
    private static final String NULL_EXCEPTION = "Exception must not be null";
    private static final String NULL_MESSAGE = "Message must not be null";
    private static final String NULL_SCRIPT = "Script must not be null";

    private static final long serialVersionUID = 0L;

    private static int requirePositive(final int value, final String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    /**
     * Construct a new Funcky exception.
     * 
     * @param script
     *            the URI of the script in which the error occurred
     * @param exception
     *            the IO error that caused this Funcky exception
     */
    protected FunckyException(final URI script, final IOException exception) {
        super(null, Objects.requireNonNull(script, NULL_SCRIPT).toString(), -1);
        Objects.requireNonNull(exception, NULL_EXCEPTION);
        initCause(exception);
    }

    /**
     * Construct a new Funcky exception.
     * 
     * @param message
     *            the error message of this Funcky exception
     * @param script
     *            the URI of the script in which the error occurred
     * @param line
     *            the line at which the error occurred
     * @param column
     *            the column at which the error occurred
     */
    protected FunckyException(final String message, final URI script, final int line,
            final int column) {
        super(Objects.requireNonNull(message, NULL_MESSAGE),
                Objects.requireNonNull(script, NULL_SCRIPT).toString(),
                requirePositive(line, NON_POSITIVE_LINE),
                requirePositive(column, NON_POSITIVE_COLUMN));
    }
}
