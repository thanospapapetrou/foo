package com.github.thanospapapetrou.funcky.script.expression.literal;

import com.github.thanospapapetrou.funcky.FunckyEngine;
import com.github.thanospapapetrou.funcky.script.expression.Expression;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.Type;

import java.net.URI;

import javax.script.ScriptContext;

public abstract class Literal extends Expression {
    protected Literal(final FunckyEngine engine, final URI script, final int line,
            final int column) {
        super(engine, script, line, column);
    }

    protected Literal() {
        super();
    }

    @Override
    public Literal eval(final ScriptContext context) {
        return this;
    }

    @Override
    public abstract Type getType(final ScriptContext context);

}
