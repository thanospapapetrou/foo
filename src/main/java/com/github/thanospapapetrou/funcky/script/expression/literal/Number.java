package com.github.thanospapapetrou.funcky.script.expression.literal;

import com.github.thanospapapetrou.funcky.FunckyEngine;
import com.github.thanospapapetrou.funcky.prelude.Numbers;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.SimpleType;

import java.net.URI;

import javax.script.ScriptContext;

public class Number extends Literal {
    private static final String INFINITY = "infinity";
    private static final String NAN = "nan";

    private final double value;

    public Number(final FunckyEngine engine, final URI script, final int line, final int column,
            final double value) {
        super(engine, script, line, column);
        this.value = value;
    }

    public Number(final double value) {
        super();
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public SimpleType getType(final ScriptContext context) {
        return Numbers.NUMBER;
    }

    @Override
    public String toString() {
        return (value == Double.POSITIVE_INFINITY) ? INFINITY
                : ((value == Double.NEGATIVE_INFINITY) ? "TODO fix minus infinity as an expression"
                        : (Double.isNaN(value) ? NAN : Double.toString(value)));
    }
}
