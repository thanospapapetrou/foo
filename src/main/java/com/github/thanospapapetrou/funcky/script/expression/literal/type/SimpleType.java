package com.github.thanospapapetrou.funcky.script.expression.literal.type;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Class representing a Funcky simple type.
 * 
 * @author thanos
 */
public class SimpleType extends Type {
    private static final String EMPTY_NAME = "Name must not be empty";
    private static final String NULL_NAME = "Name must not be null";
    private static final String NULL_TYPE = "Type must not be null";

    private final String name;

    private static void requireValidName(final String name) {
        if (Objects.requireNonNull(name, NULL_NAME).isEmpty()) {
            throw new IllegalArgumentException(EMPTY_NAME);
        }
    }

    /**
     * Construct a new simple type.
     * 
     * @param name
     *            the name of this simple type
     */
    public SimpleType(final String name) {
        requireValidName(name);
        this.name = name;
    }

    @Override
    public Type bind(final Map<TypeVariable, Type> bindings) {
        return this;
    }

    @Override
    public boolean equals(final Object object) {
        return (object instanceof SimpleType) && name.equals(((SimpleType) object).name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public Map<TypeVariable, Type> infer(final Type type) {
        Objects.requireNonNull(type, NULL_TYPE);
        return (type instanceof TypeVariable) ? Collections.singletonMap((TypeVariable) type, this)
                : (equals(type) ? Collections.emptyMap() : null);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    protected Type free(final Map<TypeVariable, TypeVariable> freed) {
        return this;
    }
}
