package com.github.thanospapapetrou.funcky.parser.exception;

import com.github.thanospapapetrou.funcky.parser.Parser;
import com.github.thanospapapetrou.funcky.parser.tokenizer.exception.UnparsableInputRuntimeException;

import java.net.URI;
import java.util.Objects;

/**
 * Exception thrown by {@link Parser} if any unparsable input is encountered while parsing.
 * 
 * @author thanos
 */
public class UnparsableInputException extends ParseException {
    private static final String NULL_EXCEPTION = "Exception must not be null";
    private static final String UNPARSABLE_INPUT = "Unparsable input \"%1$s\"";
    private static final long serialVersionUID = 0L;

    /**
     * Construct a new unparsable input exception.
     * 
     * @param script
     *            the URI of the script in which unparsable input was encountered
     * @param exception
     *            the unparsable input runtime exception that triggered this exception
     */
    public UnparsableInputException(final URI script,
            final UnparsableInputRuntimeException exception) {
        super(String.format(UNPARSABLE_INPUT,
                Objects.requireNonNull(exception, NULL_EXCEPTION).getInput()), script,
                exception.getLine(), exception.getColumn());
    }
}
