package com.github.thanospapapetrou.funcky.script.expression;

import com.github.thanospapapetrou.funcky.FunckyEngine;
import com.github.thanospapapetrou.funcky.script.expression.literal.Literal;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.Type;

import java.net.URI;

import javax.script.ScriptContext;

public class Reference extends Expression {
    private final String name;

    public Reference(final FunckyEngine engine, final URI script, final int line, final int column,
            final String name) {
        super(engine, script, line, column);
        this.name = name;
    }

    @Override
    public Literal eval(final ScriptContext context) {
        return resolve(context).eval(context);
    }

    @Override
    public Type getType(final ScriptContext context) {
        return resolve(context).getType(context);
    }

    @Override
    public String toString() {
        return name;
    }

    private Expression resolve(final ScriptContext context) {
        // TODO use custom scope
        return (Expression) context.getBindings(ScriptContext.ENGINE_SCOPE).get(name);
    }
}
