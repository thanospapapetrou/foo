package com.github.thanospapapetrou.funcky.library;

import com.github.thanospapapetrou.funcky.script.expression.Expression;
import com.github.thanospapapetrou.funcky.script.expression.literal.Literal;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.FunctionType;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.SimpleType;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.Type;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.script.ScriptContext;

public class Core extends Library {
    public static final SimpleType TYPE = new SimpleType("Type");
    public static final Functor FUNCTION =
            new Functor("function", new FunctionType(TYPE, new FunctionType(TYPE, TYPE)), 2) {

                @Override
                protected Literal apply(final ScriptContext context,
                        final Expression... arguments) {
                    return new FunctionType((Type) arguments[0].eval(context),
                            (Type) arguments[1].eval(context));
                }
            };

    private static final Set<Literal> LITERALS =
            Collections.unmodifiableSet(new HashSet<Literal>() {
                private static final long serialVersionUID = 0L;

                {
                    add(TYPE);
                    add(FUNCTION);
                }
            });
    // TODO typeOf
    // TODO function
    // TODO equals
    // TODO if

    public Core() {
        super(LITERALS);
    }
}
