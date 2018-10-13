package com.github.thanospapapetrou.funcky.parser;

import com.github.thanospapapetrou.funcky.parser.tokenizer.Token;
import com.github.thanospapapetrou.funcky.parser.tokenizer.TokenType;

import java.util.Arrays;

class UnexpectedTokenException extends ParseException {
    private static final String DELIMITER = ", ";
    private static final String UNEXPECTED_TOKEN = "Unexpected %1$s, expected %2$s";
    private static final long serialVersionUID = 0L;

    UnexpectedTokenException(final Token unexpected, final TokenType... expected) {
        super(String.format(UNEXPECTED_TOKEN, unexpected, String.join(DELIMITER,
                (Iterable<String>) Arrays.stream(expected).map(TokenType::toString)::iterator)),
                unexpected.getScript(), unexpected.getLine(), unexpected.getColumn());
    }
}
