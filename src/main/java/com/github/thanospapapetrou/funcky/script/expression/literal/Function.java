package com.github.thanospapapetrou.funcky.script.expression.literal;

import com.github.thanospapapetrou.funcky.script.expression.Expression;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.FunctionType;

import javax.script.ScriptContext;

/**
 * Abstract class representing a Funcky function.
 * 
 * @author thanos
 */
public abstract class Function extends Literal {
    private final String name;
    private final FunctionType type;

    /**
     * Construct a new function.
     * 
     * @param name
     *            the name of this function
     * @param type
     *            the type of this function
     */
    protected Function(final String name, final FunctionType type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Apply this function to the given argument as evaluated in the given context
     * 
     * @param context
     *            the context to use to evaluate argument
     * @param argument
     *            the argument to apply this function to
     * @return the result of this function when applied to the given argument as evaluated in the
     *         given context
     */
    public abstract Literal apply(final ScriptContext context, final Expression argument);

    @Override
    public FunctionType getType() {
        return type;
    }

    @Override
    public String toString() {
        return name;
    }
}
