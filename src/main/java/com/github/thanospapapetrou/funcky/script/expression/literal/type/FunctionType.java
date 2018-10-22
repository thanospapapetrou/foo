package com.github.thanospapapetrou.funcky.script.expression.literal.type;

public class FunctionType extends Type {
    private final Type domain;
    private final Type range;

    public FunctionType(final Type domain, final Type range) {
        this.domain = domain;
        this.range = range;
    }

    public Type getDomain() {
        return domain;
    }

    public Type getRange() {
        return range;
    }
}
