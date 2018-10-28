package com.github.thanospapapetrou.funcky.script.expression.literal.type;

/**
 * Class representing a Funcky simple type.
 * 
 * @author thanos
 */
public class SimpleType extends Type {
    private final String name;

    /**
     * Construct a new simple type.
     * 
     * @param name
     *            the name of this simple type
     */
    public SimpleType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
