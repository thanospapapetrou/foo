package com.github.thanospapapetrou.funcky.script.expression.literal.type;

import com.github.thanospapapetrou.funcky.library.Core;
import com.github.thanospapapetrou.funcky.script.expression.Application;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class representing a Funcky function type.§
 * 
 * @author thanos
 */
public class FunctionType extends Type {
    private static final String NULL_BINDINGS = "Bindings must not be null";
    private static final String NULL_DOMAIN = "Domain must not be null";
    private static final String NULL_FREED = "Freed must not be null";
    private static final String NULL_RANGE = "Range must not be null";
    private static final String NULL_TYPE = "Type must not be null";

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
    public Type bind(final Map<TypeVariable, Type> bindings) {
        Objects.requireNonNull(bindings, NULL_BINDINGS);
        return new FunctionType(domain.bind(bindings), range.bind(bindings));
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
    public int hashCode() {
        return domain.hashCode() + range.hashCode();
    }

    @Override
    public Map<TypeVariable, Type> infer(final Type type) {
        Objects.requireNonNull(type, NULL_TYPE);
        if (type instanceof TypeVariable) {
            return Collections.singletonMap((TypeVariable) type, this);
        } else if (type instanceof FunctionType) {
            final FunctionType functionType = (FunctionType) type;
            final Map<TypeVariable, Type> domainBindings = domain.infer(functionType.domain);
            if (domainBindings == null) {
                return null;
            }
            final Map<TypeVariable, Type> rangeBindings = range.infer(functionType.range);
            if (rangeBindings == null) {
                return null;
            }
            final Map<TypeVariable, Type> bindings = new HashMap<>();
            bindings.putAll(domainBindings);
            bindings.putAll(rangeBindings);
            return bindings;
        }
        return null;
    }

    @Override
    public String toString() {
        return new Application(new Application(Core.FUNCTION, domain), range).toString();
    }

    @Override
    protected Type free(final Map<TypeVariable, TypeVariable> freed) {
        Objects.requireNonNull(freed, NULL_FREED);
        return new FunctionType(domain.free(freed), range.free(freed));
    }
}
