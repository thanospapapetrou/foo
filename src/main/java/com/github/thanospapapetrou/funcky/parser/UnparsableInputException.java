package com.github.thanospapapetrou.funcky.parser;

import com.github.thanospapapetrou.funcky.parser.tokenizer.UnparsableInputRuntimeException;

import java.net.URI;

public class UnparsableInputException extends ParseException {
    private static final String UNPARSABLE_INPUT = "Unparsable input \"%1$s\"";
    private static final long serialVersionUID = 0L;

    UnparsableInputException(final URI script, final UnparsableInputRuntimeException e) {
        super(String.format(UNPARSABLE_INPUT, e.getInput()), script, e.getLine(), e.getColumn());
    }
}
