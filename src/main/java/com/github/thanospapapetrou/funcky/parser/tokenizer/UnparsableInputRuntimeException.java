package com.github.thanospapapetrou.funcky.parser.tokenizer;

import com.github.thanospapapetrou.funcky.parser.UnparsableInputException;

import java.net.URI;

/**
 * Exception thrown by {@link Tokenizer} whenever unparsable input is encountered. This exception is
 * a {@link RuntimeException} designed to get the necessary pieces of information past the streaming
 * tokenization process and is converted to a {@link UnparsableInputException} once it's possible to
 * throw a checked exception again.
 * 
 * @author thanos
 */
public class UnparsableInputRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 0L;

    private final String input;
    private final URI script;
    private final int line;
    private final int column;

    UnparsableInputRuntimeException(final String input, final URI script, final int line,
            final int column) {
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
     * @return the line at which unparsable input was encountered
     */
    public int getLine() {
        return line;
    }

    /**
     * Get column.
     * 
     * @return the column at which unparsable input was encountered
     */
    public int getColumn() {
        return column;
    }
}
