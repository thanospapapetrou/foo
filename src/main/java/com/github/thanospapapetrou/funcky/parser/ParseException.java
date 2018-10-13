package com.github.thanospapapetrou.funcky.parser;

import com.github.thanospapapetrou.funcky.FunckyException;

import java.io.IOException;
import java.net.URI;

class ParseException extends FunckyException {
    private static final String ERROR_AT = "%1$s at %2$s, line %3$d, column %4$d";
    private static final String ERROR_PARSING = "Error parsing %1$s";
    private static final long serialVersionUID = 0L;

    ParseException(final URI script, final IOException e) {
        super(String.format(ERROR_PARSING, script), script, e);
    }

    ParseException(final String message, final URI script, final int line, final int column) {
        super(String.format(ERROR_AT, message, script, line, column), script, line, column);
    }
}
