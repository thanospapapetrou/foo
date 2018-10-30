package com.github.thanospapapetrou.funcky.parser.exception;

import com.github.thanospapapetrou.funcky.parser.Parser;
import com.github.thanospapapetrou.funcky.parser.tokenizer.Token;
import com.github.thanospapapetrou.funcky.parser.tokenizer.TokenType;

import java.util.Arrays;
import java.util.Objects;

/**
 * Exception thrown by {@link Parser} if an unexpected token is encountered while parsing.
 * 
 * @author thanos
 */
public class UnexpectedTokenException extends ParseException {
    private static final String DELIMITER = ", ";
    private static final String EMPTY_EXPECTED = "Expected must not be empty";
    private static final String NULL_EXPECTED = "Expected must not be null";
    private static final String NULL_EXPECTED_ELEMENT = "Expected must not contain null elements";
    private static final String NULL_UNEXPECTED = "Unexpected must not be null";
    private static final String UNEXPECTED_TOKEN = "Unexpected %1$s, expected %2$s";
    private static final long serialVersionUID = 0L;

    private static TokenType[] requireValidExpected(final TokenType[] expected) {
        if (Objects.requireNonNull(expected, NULL_EXPECTED).length == 0) {
            throw new IllegalArgumentException(EMPTY_EXPECTED);
        }
        Arrays.stream(expected)
                .forEach(element -> Objects.requireNonNull(element, NULL_EXPECTED_ELEMENT));
        return expected;
    }

    /**
     * Construct a new unexpected token exception.
     * 
     * @param unexpected
     *            the unexpected token encountered
     * @param expected
     *            the tokens expected instead
     */
    public UnexpectedTokenException(final Token unexpected, final TokenType... expected) {
        super(String.format(UNEXPECTED_TOKEN, Objects.requireNonNull(unexpected, NULL_UNEXPECTED),
                String.join(DELIMITER,
                        (Iterable<String>) Arrays.stream(requireValidExpected(expected))
                                .map(TokenType::toString)::iterator)),
                unexpected.getScript(), unexpected.getLine(), unexpected.getColumn());
    }
}
