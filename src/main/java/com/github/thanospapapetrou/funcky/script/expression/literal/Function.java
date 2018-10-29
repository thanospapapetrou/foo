package com.github.thanospapapetrou.funcky.script.expression.literal;

import com.github.thanospapapetrou.funcky.script.expression.Expression;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.FunctionType;

import java.util.Objects;

import javax.script.ScriptContext;

/**
 * Abstract class representing a Funcky function.
 * 
 * @author thanos
 */
public abstract class Function extends Literal {
    private static final String EMPTY_NAME = "Name must not be empty";
    private static final String NULL_NAME = "Name must not be null";
    private static final String NULL_TYPE = "Type must not be null";

    private final String name;
    private final FunctionType type;

    private static void requireValidName(final String name) {
        Objects.requireNonNull(name, NULL_NAME);
        if (name.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_NAME);
        }
    }

    /**
     * Construct a new function.
     * 
     * @param name
     *            the name of this function
     * @param type
     *            the type of this function
     */
    protected Function(final String name, final FunctionType type) {
        requireValidName(name);
        Objects.requireNonNull(type, NULL_TYPE);
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
    public boolean equals(final Object object) {
        return (object instanceof Function) && name.equals(((Function) object).name);
    }

    @Override
    public FunctionType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
