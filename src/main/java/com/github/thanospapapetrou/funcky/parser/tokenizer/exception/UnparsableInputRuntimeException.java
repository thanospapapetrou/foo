package com.github.thanospapapetrou.funcky.parser.tokenizer.exception;

import com.github.thanospapapetrou.funcky.parser.exception.UnparsableInputException;
import com.github.thanospapapetrou.funcky.parser.tokenizer.Tokenizer;

import java.net.URI;
import java.util.Objects;

/**
 * Exception thrown by {@link Tokenizer} if any unparsable input is encountered. This exception is a
 * {@link RuntimeException} designed to get the necessary pieces of information past the streaming
 * tokenization process and is converted to a {@link UnparsableInputException} once it's possible to
 * throw a checked exception again.
 * 
 * @author thanos
 */
public class UnparsableInputRuntimeException extends RuntimeException {
    private static final String EMPTY_INPUT = "Input must not be empty";
    private static final String NON_POSITIVE_COLUMN = "Column must be positive";
    private static final String NON_POSITIVE_LINE = "Line must be positive";
    private static final String NULL_INPUT = "Input must not be null";
    private static final String NULL_SCRIPT = "Script must not be null";
    private static final long serialVersionUID = 0L;

    private final String input;
    private final URI script;
    private final int line;
    private final int column;

    private static void requireValidInput(final String input) {
        if (Objects.requireNonNull(input, NULL_INPUT).isEmpty()) {
            throw new IllegalArgumentException(EMPTY_INPUT);
        }
    }

    private static void requirePositive(final int value, final String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Construct a new unparsable input runtime exception.
     * 
     * @param input
     *            the unparsable input
     * @param script
     *            the URI of the script in which unparsable input was encountered
     * @param line
     *            the line at which unparsable input was encountered (starting from <code>1</code>)
     * @param column
     *            the column at which unparsable input was encountered (starting from
     *            <code>1</code>)
     */
    public UnparsableInputRuntimeException(final String input, final URI script, final int line,
            final int column) {
        requireValidInput(input);
        Objects.requireNonNull(script, NULL_SCRIPT);
        requirePositive(line, NON_POSITIVE_LINE);
        requirePositive(column, NON_POSITIVE_COLUMN);
        this.input = input;
        this.script = script;
        this.line = line;
        this.column = column;
    }

    /**
     * Get input.
     * 
     * @return the unparsable input
     */
    public String getInput() {
        return input;
    }

    /**
     * Get script
     * 
     * @return the URI of the script in which unparsable input was encountered
     */
    public URI getScript() {
        return script;
    }

    /**
     * Get line.
     * 
     * @return the line at which unparsable input was encountered (starting from <code>1</code>)
     */
    public int getLine() {
        return line;
    }

    /**
     * Get column.
     * 
     * @return the column at which unparsable input was encountered (starting from <code>1</code>)
     */
    public int getColumn() {
        return column;
    }
}
