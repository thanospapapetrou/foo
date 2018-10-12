package com.github.thanospapapetrou.funcky.parser.tokenizer;

public class UnparsableInputRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 0L;

    private final String input;
    private final int line;
    private final int column;

    UnparsableInputRuntimeException(final String input, final int line, final int column) {
        this.input = input;
        this.line = line;
        this.column = column;
    }

    public String getInput() {
        return input;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}
