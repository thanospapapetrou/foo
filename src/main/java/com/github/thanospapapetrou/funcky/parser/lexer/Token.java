package com.github.thanospapapetrou.funcky.parser.lexer;

import java.net.URI;

class Token {
    private final URI file;
    private final TokenType type;
    private final String value;
    private final int line;
    private final int column;

    Token(final TokenType type, final String value, final URI file, final int line,
            final int column) {
        this.type = type;
        this.value = value;
        this.file = file;
        this.line = line;
        this.column = column;
    }

    TokenType getType() {
        return type;
    }

    String getValue() {
        return value;
    }

    URI getFile() {
        return file;
    }

    int getLine() {
        return line;
    }

    int getColumn() {
        return column;
    }
}
