package com.github.thanospapapetrou.funcky.parser.tokenizer;

import java.util.regex.Pattern;

/**
 * Class representing Funcky token types.
 * 
 * @author thanos
 */
public enum TokenType {
    /**
     * Comment ('#...').
     */
    COMMENT("comment", "#.*$"),

    /**
     * Dollar ('$').
     */
    DOLLAR("dollar", "\\$"),

    /**
     * End of file.
     */
    EOF("end of file", null),

    /**
     * End of line ('\n').
     */
    EOL("end of line", null),

    /**
     * Equals ('=').
     */
    EQUALS("equals", "="),

    /**
     * Identifier.
     */
    IDENTIFIER("identifier", "[\\w&&\\D]\\w*"),

    /**
     * Left parenthesis ('(').
     */
    LEFT_PARENTHESIS("left parenthesis", "\\("),

    /**
     * Number.
     */
    NUMBER("number", "[\\+\\-]?\\d+(\\.\\d+([Ee][\\+\\-]?\\d+)?)?"),

    /**
     * Right parenthesis (')').
     */
    RIGHT_PARENTHESIS("right parenthesis", "\\)"),

    /**
     * Whitespace.
     */
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
