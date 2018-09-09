package com.github.thanospapapetrou.funcky.parser.lexer;

import java.io.BufferedReader;
import java.io.Reader;
import java.net.URI;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.stream.Stream;

class Lexer {
    private final URI file;

    Lexer(final URI file) {
        this.file = file;
    }

    Stream<Token> scan(final Reader reader) {
        return scan(new LineIterator(new BufferedReader(reader), file), 0);
    }

    private Stream<Token> scan(final Iterator<String> input, final int line) {
        return input.hasNext() ? Stream.concat(scan(input.next(), line, 0), scan(input, line + 1))
                : Stream.empty();
    }

    private Stream<Token> scan(final String input, final int line, final int column) {
        if (column < input.length()) { // TODO replace with ternary
            for (final TokenType type : TokenType.values()) { // TODO replace with stream
                if (type.getPattern() != null) {
                    final Matcher matcher = type.getPattern().matcher(input.substring(column));
                    if (matcher.lookingAt()) {
                        return Stream.concat(
                                Stream.of(new Token(type, matcher.group(), file, line + 1,
                                        column + matcher.start() + 1)),
                                scan(input, line, column + matcher.end()));
                    }
                }
            }
            throw new LexerException(input.substring(column), file, line, column);
        }
        return Stream.of(new Token(TokenType.NEW_LINE, null, file, line + 1, column + 1));
    }
}
