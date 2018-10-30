package com.github.thanospapapetrou.funcky.parser.exception;

import com.github.thanospapapetrou.funcky.parser.Parser;

import java.io.IOException;
import java.net.URI;

/**
 * Exception thrown by {@link Parser} if an {@link IOException} occurs while parsing.
 * 
 * @author thanos
 */
public class ParsingErrorException extends ParseException {
    private static final String ERROR_PARSING = "Error parsing %1$s";
    private static final long serialVersionUID = 0L;

    /**
     * Construct a new parsing error exception.
     * 
     * @param script
     *            the URI of the script in which the error occurred
     * @param e
     *            the IO error that caused this parsing error exception
     */
    public ParsingErrorException(final URI script, final IOException e) {
        super(script, e);
    }

    @Override
    public String getMessage() {
        return String.format(ERROR_PARSING, getFileName());
    }

}
