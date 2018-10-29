package com.github.thanospapapetrou.funcky.script.expression.literal.type;

import com.github.thanospapapetrou.funcky.library.Core;
import com.github.thanospapapetrou.funcky.script.expression.Application;

import java.util.Objects;

/**
 * Class representing a Funcky function type.§
 * 
 * @author thanos
 */
public class FunctionType extends Type {
    private static final String NULL_DOMAIN = "Domain must not be null";
    private static final String NULL_RANGE = "Range must not be null";

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
        Objects.requireNonNull(domain, NULL_DOMAIN);
        Objects.requireNonNull(range, NULL_RANGE);
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

    @Override
    public int hashCode() {
        return domain.hashCode() + range.hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof FunctionType) {
            final FunctionType functionType = (FunctionType) object;
            return domain.equals(functionType.domain) && range.equals(functionType.range);
        }
        return false;
    }

    @Override
    public String toString() {
        return new Application(new Application(Core.FUNCTION, domain), range).toString();
    }
}
