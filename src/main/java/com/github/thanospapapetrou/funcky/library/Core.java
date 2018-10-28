package com.github.thanospapapetrou.funcky.library;

import com.github.thanospapapetrou.funcky.script.expression.literal.Literal;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.SimpleType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Core extends Library {
    public static final SimpleType TYPE = new SimpleType("Type");

    private static final Set<Literal> LITERALS =
            Collections.unmodifiableSet(new HashSet<Literal>() {
                private static final long serialVersionUID = 0L;

                {
                    add(TYPE);
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
