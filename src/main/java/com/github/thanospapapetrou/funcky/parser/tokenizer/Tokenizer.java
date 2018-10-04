package com.github.thanospapapetrou.funcky.parser.tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Tokenizer {
    private final LineIterator input;
    private final URI script;
    private int line;
    private int column;

    public Tokenizer(final Reader reader, final URI script) throws IOException {
        this.input = new LineIterator(new BufferedReader(reader), script);
        this.script = script;
        line = 0;
        column = 0;
    }

    public Stream<Token> tokenize() {
        return Stream.concat(
                StreamSupport
                        .stream(Spliterators.spliteratorUnknownSize(input, Spliterator.ORDERED),
                                false)
                        .flatMap(this::tokenize),
                Stream.generate(() -> token(TokenType.EOF, null)).limit(1L));
    }

    private Stream<Token> tokenize(final String input) {
        final List<Token> tokens = new ArrayList<>();
        while (column < input.length()) {
            tokens.add(Arrays.stream(TokenType.values()).map(type -> tokenize(input, type))
                    .filter(Objects::nonNull).findAny()
                    .orElseThrow(() -> new UnparsableInputException(input.substring(column), script,
                            line + 1, column + 1)));
        }
        tokens.add(token(TokenType.EOL, null));
        line++;
        column = 0;
        return tokens.stream();
    }

    private Token tokenize(final String input, final TokenType type) {
        final Pattern pattern = type.getPattern();
        if (pattern != null) {
            final Matcher matcher = pattern.matcher(input.substring(column));
            if (matcher.lookingAt()) {
                final Token token = token(type, matcher.group());
                column += matcher.end();
                return token;
            }
        }
        return null;
    }

    private Token token(final TokenType type, final String value) {
        return new Token(type, value, script, line + 1, column + 1);
    }
}
