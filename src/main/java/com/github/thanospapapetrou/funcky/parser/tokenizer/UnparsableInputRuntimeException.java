package com.github.thanospapapetrou.funcky.parser.tokenizer;

import java.net.URI;

public class UnparsableInputRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 0L;

    private final String input;
    private final URI script;
    private final int line;
    private final int column;

    UnparsableInputRuntimeException(final String input, final URI script, final int line,
            final int column) {
        this.input = input;
        this.script = script;
        this.line = line;
        this.column = column;
    }

    public String getInput() {
        return input;
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
}
