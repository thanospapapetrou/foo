package com.github.thanospapapetrou.funcky.script.expression.literal;

import com.github.thanospapapetrou.funcky.script.expression.Expression;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.FunctionType;

import javax.script.ScriptContext;

public abstract class Function extends Literal
        implements java.util.function.Function<Expression, Literal> {
    private final FunctionType type;

    protected Function(final FunctionType type) {
        super();
        this.type = type;
    }

    @Override
    public abstract Literal apply(final Expression argument);

    @Override
    public FunctionType getType(final ScriptContext context) {
        return type;
    }

}
