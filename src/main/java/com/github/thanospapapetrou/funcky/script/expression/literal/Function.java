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
    private static final String NULL_EXPRESSION = "Expression must not be null";
    private static final String NULL_TYPE = "Type must not be null";

    private final Expression expression;
    private final FunctionType type;

    /**
     * Construct a new function.
     * 
     * @param expression
     *            the expression to represent this function
     * @param type
     *            the type of this function
     */
    protected Function(final Expression expression, final FunctionType type) {
        Objects.requireNonNull(expression, NULL_EXPRESSION);
        Objects.requireNonNull(type, NULL_TYPE);
        this.expression = expression;
        this.type = type;
    }

    /**
     * Apply this function to the given argument as evaluated in the given context
     * 
     * @param context
     *            the context to use to evaluate argument
     * @param argument
     *            the argument to apply this function to
     * @return the result of this function applied to the given argument as evaluated in the given
     *         context
     */
    public abstract Literal apply(final ScriptContext context, final Expression argument);

    @Override
    public boolean equals(final Object object) {
        return (object instanceof Function) && expression.equals(((Function) object).expression);
    }

    @Override
    public int hashCode() {
        return expression.hashCode();
    }

    @Override
    public FunctionType getType() {
        return type;
    }

    @Override
    protected Expression toExpression() {
        return expression;
    }
}
