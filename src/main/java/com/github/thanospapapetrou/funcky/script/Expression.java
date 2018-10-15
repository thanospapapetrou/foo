package com.github.thanospapapetrou.funcky.script;

import com.github.thanospapapetrou.funcky.FunckyEngine;
import com.github.thanospapapetrou.funcky.FunckyException;

import javax.script.CompiledScript;
import javax.script.ScriptContext;

public abstract class Expression extends CompiledScript {
    private final FunckyEngine engine;

    protected Expression(final FunckyEngine engine) {
        this.engine = engine;
    }

    @Override
    // TODO change funcky exception to runtime exception
    public abstract Number eval(final ScriptContext context) throws FunckyException;

    @Override
    public FunckyEngine getEngine() {
        return engine;
    }

}
