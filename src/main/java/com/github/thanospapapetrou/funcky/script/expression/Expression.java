package com.github.thanospapapetrou.funcky.script.expression;

import com.github.thanospapapetrou.funcky.FunckyEngine;
import com.github.thanospapapetrou.funcky.script.expression.literal.Literal;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.Type;

import java.net.URI;

import javax.script.CompiledScript;
import javax.script.ScriptContext;

public abstract class Expression extends CompiledScript {
    private final FunckyEngine engine;
    private final URI script;
    private final int line;
    private final int column;

    protected Expression(final FunckyEngine engine, final URI script, final int line,
            final int column) {
        this.engine = engine;
        this.script = script;
        this.line = line;
        this.column = column;
    }

    protected Expression() {
        this(null, null, -1, -1);
    }

    public abstract Type getType(final ScriptContext context);

    @Override
    // TODO change funcky exception to runtime exception
    public abstract Literal eval(final ScriptContext context);

    @Override
    public FunckyEngine getEngine() {
        return engine;
    }

    public URI getScript() {
        return script;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}
