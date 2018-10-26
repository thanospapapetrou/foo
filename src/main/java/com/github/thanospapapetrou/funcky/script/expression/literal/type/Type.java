package com.github.thanospapapetrou.funcky.script.expression.literal.type;

import com.github.thanospapapetrou.funcky.library.Core;
import com.github.thanospapapetrou.funcky.script.expression.literal.Literal;

public abstract class Type extends Literal {
    protected Type() {
        super();
    }

    @Override
    public SimpleType getType() {
        return Core.TYPE;
    }
}
