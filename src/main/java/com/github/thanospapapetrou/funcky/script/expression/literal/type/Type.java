package com.github.thanospapapetrou.funcky.script.expression.literal.type;

import com.github.thanospapapetrou.funcky.prelude.Core;
import com.github.thanospapapetrou.funcky.script.expression.literal.Literal;

import javax.script.ScriptContext;

public abstract class Type extends Literal {
    protected Type() {
        super();
    }

    @Override
    public SimpleType getType(final ScriptContext context) {
        return Core.TYPE;
    }
}
