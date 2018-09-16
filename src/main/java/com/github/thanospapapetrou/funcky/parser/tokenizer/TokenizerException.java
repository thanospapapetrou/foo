package com.github.thanospapapetrou.funcky.parser.tokenizer;

import java.net.URI;

class TokenizerException extends RuntimeException {
    private static final String UNPARSABLE_INPUT =
            "Unparsable input \"%1$s\" at %2$s, line %3$d, column %4$d";
    private static final long serialVersionUID = 0L;

    TokenizerException(final String input, final URI file, final int line, final int column) {
        super(String.format(UNPARSABLE_INPUT, input, file, line, column));
    }
}
