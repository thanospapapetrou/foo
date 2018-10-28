package com.github.thanospapapetrou.funcky.script.expression.literal.type;

import com.github.thanospapapetrou.funcky.library.Core;
import com.github.thanospapapetrou.funcky.script.expression.literal.Literal;

/**
 * Abstract class representing a Funcky type.
 * 
 * @author thanos
 */
public abstract class Type extends Literal {
    @Override
    public SimpleType getType() {
        return Core.TYPE;
    }
}
