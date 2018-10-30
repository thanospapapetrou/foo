package com.github.thanospapapetrou.funcky.script.expression;

import com.github.thanospapapetrou.funcky.FunckyEngine;
import com.github.thanospapapetrou.funcky.script.expression.exception.TypeCheckException;
import com.github.thanospapapetrou.funcky.script.expression.literal.Literal;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.Type;

import java.net.URI;
import java.util.Objects;

import javax.script.CompiledScript;
import javax.script.ScriptContext;

/**
 * Abstract class representing a Funcky expression.
 * 
 * @author thanos
 */
public abstract class Expression extends CompiledScript {
    private static final String NEGATIVE_COLUMN = "Column must not be negative";
    private static final String NEGATIVE_LINE = "Line must not be negative";
    private static final String NULL_ENGINE = "Engine must not be null";
    private static final String NULL_SCRIPT = "Script must not be null";

    private final FunckyEngine engine;
    private final URI script;
    private final int line;
    private final int column;

    private static void requireNonNegative(final int value, final String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Construct a new expression.
     * 
     * @param engine
     *            the engine that compiled this expression
     * @param script
     *            the URI of the script in which this expression was encountered
     * @param line
     *            the line of the script at which this expression was encountered (starting from
     *            <code>1</code>)
     * @param column
     *            the column of the script at which this expression was encountered (starting from
     *            <code>1</code>)
     */
    protected Expression(final FunckyEngine engine, final URI script, final int line,
            final int column) {
        Objects.requireNonNull(engine, NULL_ENGINE);
        Objects.requireNonNull(script, NULL_SCRIPT);
        requireNonNegative(line, NEGATIVE_LINE);
        requireNonNegative(column, NEGATIVE_COLUMN);
        this.engine = engine;
        this.script = script;
        this.line = line;
        this.column = column;
    }

    /**
     * Construct a new expression.
     */
    protected Expression() {
        engine = null;
        script = null;
        line = -1;
        column = -1;
    }

    /**
     * Check this expression.
     * 
     * @param context
     *            the context in which to check this expression
     * @throws TypeCheckException
     *             if an error occurs while checking the type of this expression
     */
    public abstract void check(final ScriptContext context) throws TypeCheckException;

    /**
     * Get column.
     * 
     * @return the column of the script at which this expression was encountered or <code>-1</code>
     *         if this expression was generated at runtime
     */
    public int getColumn() {
        return column;
    }

    /**
     * Get line.
     * 
     * @return the line of the script at which this expression was encountered or <code>-1</code> if
     *         this expression was generated at runtime
     */
    public int getLine() {
        return line;
    }

    /**
     * Get script.
     * 
     * @return the URI of the script in which this expression was encountered or <code>null</code>
     *         if this expression was generated at runtime
     */
    public URI getScript() {
        return script;
    }

    /**
     * Get type.
     * 
     * @param context
     *            the context in which to evaluate type
     * @return the type of this expression as evaluated in the given context
     */
    public abstract Type getType(final ScriptContext context);

    @Override
    public abstract Literal eval(final ScriptContext context);

    @Override
    public FunckyEngine getEngine() {
        return engine;
    }
}
