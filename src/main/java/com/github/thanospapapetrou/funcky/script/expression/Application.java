package com.github.thanospapapetrou.funcky.script.expression;

import com.github.thanospapapetrou.funcky.FunckyEngine;
import com.github.thanospapapetrou.funcky.script.expression.literal.Function;
import com.github.thanospapapetrou.funcky.script.expression.literal.Literal;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.FunctionType;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.Type;

import java.net.URI;

import javax.script.ScriptContext;

public class Application extends Expression {
    private static final String APPLICATION_FORMAT = "%1$s %2$s";
    private static final String NESTED_APPLICATION = "(%1$s)";

    private final Expression function;
    private final Expression argument;

    public Application(final FunckyEngine engine, final URI script, final int line,
            final int column, final Expression function, final Expression argument) {
        super(engine, script, line, column);
        this.function = function;
        this.argument = argument;
    }

    @Override
    public Literal eval(final ScriptContext context) {
        // TODO function may not be a function
        return ((Function) function.eval(context)).apply(argument);
    }

    @Override
    public Type getType(final ScriptContext context) {
        return ((FunctionType) function.getType(context)).getRange();
    }

    @Override
    public String toString() {
        return String.format(APPLICATION_FORMAT, function, (argument instanceof Application)
                ? String.format(NESTED_APPLICATION, argument) : argument);
    }
}
