package com.github.thanospapapetrou.funcky.parser.tokenizer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;

/**
 * Exception thrown by {@link LineIterator} whenever an {@link IOException} occurs while parsing a
 * script. This exception is a {@link RuntimeException} designed to get the necessary pieces of
 * information past the streaming tokenization process and is converted to a
 * {@link ReadingException} once it's possible to throw a checked exception again.
 * 
 * @author thanos
 */
public class ReadingException extends UncheckedIOException {
    private static final long serialVersionUID = 0L;

    private final URI script;

    ReadingException(final URI script, final IOException e) {
        super(e);
        this.script = script;
    }

    /**
     * Get script.
     * 
     * @return the URI of the script during whose parsing the {@link IOException} occured
     */
    public URI getScript() {
        return script;
    }
}
