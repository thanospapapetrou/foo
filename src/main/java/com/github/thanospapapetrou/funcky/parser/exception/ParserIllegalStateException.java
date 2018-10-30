package com.github.thanospapapetrou.funcky.parser.exception;

import com.github.thanospapapetrou.funcky.parser.Parser;
import com.github.thanospapapetrou.funcky.parser.tokenizer.Token;

import java.util.Objects;

/**
 * Exception thrown by {@link Parser} if it ends up in an illegal state where a token, although
 * expected, has not been handled while parsing.
 * 
 * @author thanos
 */
public class ParserIllegalStateException extends IllegalStateException {
    private static final String ILLEGAL_STATE =
            "Parser is in illegal state, token %1$s (%2$s) had been expected but was not handled in %3$s at line %4$s, column %5$s";
    private static final String NULL_TOKEN = "Token must not be null";
    private static final long serialVersionUID = 0L;

    /**
     * Construct a new parser illegal state exception.
     * 
     * @param token
     *            the token that caused the parser illegal state exception
     */
    public ParserIllegalStateException(final Token token) {
        super(String.format(ILLEGAL_STATE, Objects.requireNonNull(token, NULL_TOKEN).getType(),
                token.getValue(), token.getScript(), token.getLine(), token.getColumn()));
    }
}
