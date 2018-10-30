package com.github.thanospapapetrou.funcky.parser.exception;

import com.github.thanospapapetrou.funcky.FunckyException;
import com.github.thanospapapetrou.funcky.parser.Parser;

import java.io.IOException;
import java.net.URI;

/**
 * Abstract exception thrown by {@link Parser} if an error occurs while parsing.
 * 
 * @author thanos
 */
public abstract class ParseException extends FunckyException {
    private static final long serialVersionUID = 0L;

    /**
     * Construct a new parse exception.
     * 
     * @param script
     *            the URI of the script in which the error occurred
     * @param e
     *            the IO error that caused this parse exception
     */
    protected ParseException(final URI script, final IOException e) {
        super(script, e);
    }

    /**
     * Construct a new parse exception.
     * 
     * @param message
     *            the error message of this parse exception
     * @param script
     *            the URI of the script in which the error occurred
     * @param line
     *            the line at which the error occurred (starting from <code>1</code>)
     * @param column
     *            the column at which the error occurred (starting from <code>1</code>)
     */
    protected ParseException(final String message, final URI script, final int line,
            final int column) {
        super(message, script, line, column);
    }
}
