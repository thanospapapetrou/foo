package com.github.thanospapapetrou.funcky.script.expression.literal.type;

public class SimpleType extends Type {
    private final String name;

    public SimpleType(final String name) {
        super();
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
