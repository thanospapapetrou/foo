package com.github.thanospapapetrou.funcky.parser;

import com.github.thanospapapetrou.funcky.FunckyEngine;

import javax.script.ScriptContext;

public class Number extends Expression {
    private final double value;

    Number(final FunckyEngine engine, final double value) {
        super(engine);
        this.value = value;
    }

    @Override
    public Number eval(final ScriptContext context) {
        return this;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
