package com.github.thanospapapetrou.funcky.script.expression.literal.type;

import com.github.thanospapapetrou.funcky.FunckyEngine;

import java.net.URI;
import java.util.Objects;
import java.util.UUID;

public class TypeVariable extends Type {
    private static final String EMPTY_NAME = "Name must not be empty";
    private static final String FORMAT = "$%1$s";
    private static final String RANDOM_NAME = "_%1$s%2$s";
    private static final String NULL_NAME = "Name must not be null";

    private final String name;

    private static void requireValidName(final String name) {
        if (Objects.requireNonNull(name, NULL_NAME).isEmpty()) {
            throw new IllegalArgumentException(EMPTY_NAME);
        }
    }

    public TypeVariable(final FunckyEngine engine, final URI script, final int line,
            final int column, final String name) {
        super(engine, script, line, column);
        requireValidName(name);
        this.name = name;
    }

    public TypeVariable() {
        super();
        final UUID uuid = UUID.randomUUID();
        this.name = String.format(RANDOM_NAME, Long.toHexString(uuid.getMostSignificantBits()),
                Long.toHexString(uuid.getLeastSignificantBits()));
    }

    @Override
    public boolean equals(final Object object) {
        return (object instanceof TypeVariable) && name.equals(((TypeVariable) object).name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return String.format(FORMAT, name);
    }
}
