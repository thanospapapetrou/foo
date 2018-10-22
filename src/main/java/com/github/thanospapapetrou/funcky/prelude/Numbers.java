package com.github.thanospapapetrou.funcky.prelude;

import com.github.thanospapapetrou.funcky.script.expression.Expression;
import com.github.thanospapapetrou.funcky.script.expression.literal.Function;
import com.github.thanospapapetrou.funcky.script.expression.literal.Literal;
import com.github.thanospapapetrou.funcky.script.expression.literal.Number;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.FunctionType;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.SimpleType;

import javax.script.ScriptException;

public class Numbers {
    // TODO string prefix, map<string, literal>?
    public static final SimpleType NUMBER = new SimpleType("Number");
    public static final Number NAN = new Number(Double.NaN);
    public static final Number INFINITY = new Number(Double.POSITIVE_INFINITY);
    // TODO minus = subtract 0
    public static final Function MINUS = new Function(new FunctionType(NUMBER, NUMBER)) {
        @Override
        public Literal apply(final Expression argument) {
            try {
                return new Number(-((Number) argument.eval()).getValue());
            } catch (final ScriptException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }
    };
    // TODO add subtract multiply divide modulo minus
}
