package com.github.thanospapapetrou.funcky.parser.tokenizer;

import java.io.IOException;
import java.io.UncheckedIOException;

public class ReadingException extends UncheckedIOException {
    private static final long serialVersionUID = 0L;

    ReadingException(final IOException e) {
        super(e);
    }
}
