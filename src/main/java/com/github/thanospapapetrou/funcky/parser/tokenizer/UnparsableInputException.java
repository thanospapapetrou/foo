package com.github.thanospapapetrou.funcky.parser.tokenizer;

import java.net.URI;

class UnparsableInputException extends RuntimeException {
    private static final String UNPARSABLE_INPUT =
            "Unparsable input \"%1$s\" at %2$s, line %3$d, column %4$d";
    private static final long serialVersionUID = 0L;

    UnparsableInputException(final String input, final URI script, final int line, final int column) {
        super(String.format(UNPARSABLE_INPUT, input, script, line, column));
    }
}
