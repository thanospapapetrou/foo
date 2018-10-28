package com.github.thanospapapetrou.funcky.script.expression.literal.type;

/**
 * Class representing a Funcky function type.§
 * 
 * @author thanos
 */
public class FunctionType extends Type {
    private final Type domain;
    private final Type range;

    /**
     * Construct a new function type.
     * 
     * @param domain
     *            the domain of this function type
     * @param range
     *            the range of this function type
     */
    public FunctionType(final Type domain, final Type range) {
        this.domain = domain;
        this.range = range;
    }

    /**
     * Get domain.
     * 
     * @return the domain of this function type
     */
    public Type getDomain() {
        return domain;
    }

    /**
     * Get range.
     * 
     * @return the range of this function type
     */
    public Type getRange() {
        return range;
    }
}
