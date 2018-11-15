package com.github.thanospapapetrou.funcky.library;

import com.github.thanospapapetrou.funcky.script.expression.Application;
import com.github.thanospapapetrou.funcky.script.expression.Expression;
import com.github.thanospapapetrou.funcky.script.expression.literal.Function;
import com.github.thanospapapetrou.funcky.script.expression.literal.Literal;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.FunctionType;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.Type;

import javax.script.ScriptContext;

abstract class Functor extends Function {
    private final int arguments; // TODO get rid of arguments

    protected Functor(final Expression expression, final FunctionType type, final int arguments) {
        super(expression, type);
        // TODO validate arguments
        this.arguments = arguments;
    }

    @Override
    public Literal apply(final ScriptContext context, final Expression argument) {
        final Functor that = this;
        // TODO use inference for generic types
        final Type newType = this.getType().getRange();
        return (arguments > 1) ? new Functor(new Application(that, argument),
                (FunctionType) newType, arguments - 1) {
            @Override
            public Literal apply(final ScriptContext context, final Expression... arguments) {
                final Expression[] newArguments = new Expression[arguments.length + 1];
                newArguments[0] = argument;
                System.arraycopy(arguments, 0, newArguments, 1, arguments.length);
                return that.apply(context, newArguments);
            }
        } : apply(context, new Expression[] {argument});
    }

    protected abstract Literal apply(final ScriptContext context, final Expression... arguments);
}
