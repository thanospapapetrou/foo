package com.github.thanospapapetrou.funcky.script;

import com.github.thanospapapetrou.funcky.FunckyEngine;
import com.github.thanospapapetrou.funcky.FunckyException;

import javax.script.ScriptContext;

public class Reference extends Expression {
    private final String name;

    public Reference(final FunckyEngine engine, final String name) {
        super(engine);
        this.name = name;
    }

    @Override
    public Number eval(final ScriptContext context) throws FunckyException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
