package com.github.thanospapapetrou.funcky.parser;

import com.github.thanospapapetrou.funcky.parser.tokenizer.Token;

class ParserIllegalStateException extends IllegalStateException {
    private static final String ILLEGAL_STATE =
            "Parser is in illegal state, token %1$s (%2$s) had been expected but was not handled in %3$s at line %4$s, column %5$s";
    private static final long serialVersionUID = 0L;

    ParserIllegalStateException(final Token token) {
        super(String.format(ILLEGAL_STATE, token.getType(), token.getValue(), token.getScript(),
                token.getLine(), token.getColumn()));
    }
}
