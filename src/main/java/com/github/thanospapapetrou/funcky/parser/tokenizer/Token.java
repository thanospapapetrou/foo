package com.github.thanospapapetrou.funcky.parser.tokenizer;

import java.net.URI;

public class Token {
    private static final String FORMAT = "%1$s '%2$s'";

    private final TokenType type;
    private final String value;
    private final URI script;
    private final int line;
    private final int column;

    Token(final TokenType type, final String value, final URI script, final int line,
            final int column) {
        this.type = type;
        this.value = value;
        this.script = script;
        this.line = line;
        this.column = column;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public URI getScript() {
        return script;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return (value == null) ? type.toString() : String.format(FORMAT, type, value);
    }
}
