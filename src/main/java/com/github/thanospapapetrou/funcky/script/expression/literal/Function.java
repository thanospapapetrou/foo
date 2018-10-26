package com.github.thanospapapetrou.funcky.script.expression.literal;

import com.github.thanospapapetrou.funcky.script.expression.Expression;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.FunctionType;

import javax.script.ScriptContext;

public abstract class Function extends Literal {
    private final String name;
    private final FunctionType type;

    protected Function(final String name, final FunctionType type) {
        super();
        this.name = name;
        this.type = type;
    }

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
