package com.github.thanospapapetrou.funcky.library;

import com.github.thanospapapetrou.funcky.script.expression.literal.Literal;

import java.util.Set;

public abstract class Library { // TODO extends script
    private final Set<Literal> literals;

    protected Library(final Set<Literal> literals) {
        this.literals = literals;
    }

    public Set<Literal> getLiterals() {
        return literals;
    }
}
