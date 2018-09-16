package com.github.thanospapapetrou.funcky.parser.tokenizer;

import java.util.regex.Pattern;

enum TokenType {
    COMMENT("comment", "#.*$"),
    EOF("end of file", null),
    EOL("end of line", null),
    EQUAL("equal", "="),
    IDENTIFIER("identifier", "[\\w&&\\D]\\w*"),
    LEFT_PARENTHESIS("left parenthesis", "\\("),
    NUMBER("number", "[\\+\\-]?\\d+(\\.\\d+)?"),
    RIGHT_PARENTHESIS("right parenthesis", "\\)"),
    WHITESPACE("whitespace", "\\s+");

    private final String name;
    private final Pattern pattern;

    private TokenType(final String name, final String pattern) {
        this.name = name;
        this.pattern = (pattern == null) ? null : Pattern.compile(pattern);
    }

    @Override
    public String toString() {
        return name;
    }

    Pattern getPattern() {
        return pattern;
    }
}
