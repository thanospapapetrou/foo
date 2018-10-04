package com.github.thanospapapetrou.funcky.parser;

import com.github.thanospapapetrou.funcky.FunckyEngine;
import com.github.thanospapapetrou.funcky.FunckyException;

import javax.script.ScriptContext;

class Application extends Expression {
    private static final String APPLICATION_FORMAT = "%1$s %2$s";
    private static final String NESTED_APPLICATION = "(%1$s)";

    private final Expression function;
    private final Expression argument;

    Application(final FunckyEngine engine, final Expression function, final Expression argument) {
        super(engine);
        this.function = function;
        this.argument = argument;
    }

    @Override
    public Number eval(final ScriptContext context) throws FunckyException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        return String.format(APPLICATION_FORMAT, function, (argument instanceof Application)
                ? String.format(NESTED_APPLICATION, argument) : argument);
    }
}
