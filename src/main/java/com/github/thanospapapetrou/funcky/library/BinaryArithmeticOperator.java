package com.github.thanospapapetrou.funcky.library;

import com.github.thanospapapetrou.funcky.script.expression.Expression;
import com.github.thanospapapetrou.funcky.script.expression.literal.Number;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.FunctionType;

import javax.script.ScriptContext;

abstract class BinaryArithmeticOperator extends Functor {
    private static final FunctionType TYPE =
            new FunctionType(Numbers.NUMBER, new FunctionType(Numbers.NUMBER, Numbers.NUMBER));

    protected BinaryArithmeticOperator(final String name) {
        super(name, TYPE, 2);
    }

    protected abstract double apply(final double a, final double b);

    @Override
    public Number apply(final ScriptContext context, final Expression... arguments) {
        // TODO what about this context?
        return new Number(apply(((Number) arguments[0].eval(context)).getValue(),
                ((Number) arguments[1].eval(context)).getValue()));
    }
}
