package com.github.thanospapapetrou.funcky.parser.tokenizer.exception;

import com.github.thanospapapetrou.funcky.parser.exception.ParsingErrorException;
import com.github.thanospapapetrou.funcky.parser.tokenizer.LineIterator;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.Objects;

/**
 * Exception thrown by {@link LineIterator} if an {@link IOException} occurs while parsing a script.
 * This exception is a {@link RuntimeException} designed to get the necessary pieces of information
 * past the streaming tokenization process and is converted to a {@link ParsingErrorException} once
 * it's possible to throw a checked exception again.
 * 
 * @author thanos
 */
public class ReadingException extends UncheckedIOException {
    private static final String NULL_SCRIPT = "Script must not be null";
    private static final long serialVersionUID = 0L;

    private final URI script;

    /**
     * Construct a new reading exception.
     * 
     * @param script
     *            the URI of the script during whose parsing an IO exception occurred
     * @param e
     *            the IO exception to be wrapped by this exception
     */
    public ReadingException(final URI script, final IOException e) {
        super(e);
        Objects.requireNonNull(script, NULL_SCRIPT);
        this.script = script;
    }

    /**
     * Get script.
     * 
     * @return the URI of the script during whose parsing the {@link IOException} occurred
     */
    public URI getScript() {
        return script;
    }
}
