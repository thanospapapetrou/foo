package com.github.thanospapapetrou.funcky.parser.tokenizer;

import java.net.URI;

/**
 * Class representing a Funcky token.
 * 
 * @author thanos
 */
public class Token {
    private static final String FORMAT = "%1$s \"%2$s\"";

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

    /**
     * Get type.
     * 
     * @return the type of this token
     */
    public TokenType getType() {
        return type;
    }

    /**
     * Get value.
     * 
     * @return the value of this token
     */
    public String getValue() {
        return value;
    }

    /**
     * Get script.
     * 
     * @return the script in which this token was encountered
     */
    public URI getScript() {
        return script;
    }

    /**
     * Get line.
     * 
     * @return line the line in which this token was encountered (starting from <code>1</code>)
     */
    public int getLine() {
        return line;
    }

    /**
     * Get column.
     * 
     * @return column the column in which this token was encountered (starting from <code>1</code>)
     */
    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return (value == null) ? type.toString() : String.format(FORMAT, type, value);
    }
}
