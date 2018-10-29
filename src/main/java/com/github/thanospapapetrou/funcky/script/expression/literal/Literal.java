package com.github.thanospapapetrou.funcky.script.expression.literal;

import com.github.thanospapapetrou.funcky.FunckyEngine;
import com.github.thanospapapetrou.funcky.script.expression.Expression;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.Type;

import java.net.URI;

import javax.script.ScriptContext;

/**
 * Abstract class representing a Funcky literal.
 * 
 * @author thanos
 */
public abstract class Literal extends Expression {
    /**
     * Construct a new literal.
     * 
     * @param engine
     *            the engine that compiled this literal
     * @param script
     *            the URI of the script in which this literal was encountered
     * @param line
     *            the line of the script at which this literal was encountered
     * @param column
     *            the column of the script at which this literal was encountered
     */
    protected Literal(final FunckyEngine engine, final URI script, final int line,
            final int column) {
        super(engine, script, line, column);
    }

    /**
     * Construct a new literal.
     */
    protected Literal() {
        super();
    }

    /**
     * Get type.
     * 
     * @return the type of this literal
     */
    public abstract Type getType();

    @Override
    public void check(final ScriptContext context) {
    }

    @Override
    public Literal eval(final ScriptContext context) {
        return this;
    }

    @Override
    public Type getType(final ScriptContext context) {
        return getType();
    }
}
