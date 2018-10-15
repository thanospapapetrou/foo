package com.github.thanospapapetrou.funcky.parser;

import com.github.thanospapapetrou.funcky.FunckyException;

import java.io.IOException;
import java.net.URI;

abstract class ParseException extends FunckyException {
    private static final long serialVersionUID = 0L;

    protected ParseException(final URI script, final IOException e) {
        super(script, e);
    }

    protected ParseException(final String message, final URI script, final int line,
            final int column) {
        super(message, script, line, column);
    }
}
