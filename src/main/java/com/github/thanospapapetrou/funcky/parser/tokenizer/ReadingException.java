package com.github.thanospapapetrou.funcky.parser.tokenizer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;

public class ReadingException extends UncheckedIOException {
    private static final long serialVersionUID = 0L;

    private final URI script;

    ReadingException(final URI script, final IOException e) {
        super(e);
        this.script = script;
    }

    public URI getScript() {
        return script;
    }
}
