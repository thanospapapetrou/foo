package com.github.thanospapapetrou.funcky.parser;

import com.github.thanospapapetrou.funcky.FunckyException;
import com.github.thanospapapetrou.funcky.parser.tokenizer.UnparsableInputRuntimeException;

import java.io.IOException;
import java.net.URI;

class ParseException extends FunckyException {
    private static final String ERROR_PARSING = "Error parsing %1$s";

    ParseException(final URI script, final IOException e) {
        super(String.format(ERROR_PARSING, script), script, e);
    }

    ParseException(final URI script, final UnparsableInputRuntimeException e) {
        super(script, e.getLine(), e.getColumn());
    }
}
