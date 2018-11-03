package com.github.thanospapapetrou.funcky.script.expression.literal.type;

import com.github.thanospapapetrou.funcky.FunckyEngine;
import com.github.thanospapapetrou.funcky.library.Core;
import com.github.thanospapapetrou.funcky.script.expression.literal.Literal;

import java.net.URI;

/**
 * Abstract class representing a Funcky type.
 * 
 * @author thanos
 */
public abstract class Type extends Literal {
    protected Type(final FunckyEngine engine, final URI script, final int line, final int column) {
        super(engine, script, line, column);
    }

    protected Type() {
        super();
    }

    @Override
    public SimpleType getType() {
        return Core.TYPE;
    }
}
