package com.github.thanospapapetrou.funcky.script.expression;

import com.github.thanospapapetrou.funcky.FunckyEngine;
import com.github.thanospapapetrou.funcky.script.expression.literal.Literal;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.Type;

import java.net.URI;
import java.util.Objects;

import javax.script.ScriptContext;

/**
 * Class representing a reference.
 * 
 * @author thanos
 */
public class Reference extends Expression {
    private static final String EMPTY_NAME = "Name must not be empty";
    private static final String NULL_CONTEXT = "Context must not be null";
    private static final String NULL_NAME = "Name must not be null";

    private final String name;

    private static void requireValidName(final String name) {
        if (Objects.requireNonNull(name, NULL_NAME).isEmpty()) {
            throw new IllegalArgumentException(EMPTY_NAME);
        }
    }

    /**
     * Construct a new reference.
     * 
     * @param engine
     *            the engine that compiled this reference
     * @param script
     *            the URI of the script in which this reference was encountered
     * @param line
     *            the line of the script at which this reference was encountered
     * @param column
     *            the column of the script at which this reference was encountered
     * @param name
     *            the name of this reference
     */
    public Reference(final FunckyEngine engine, final URI script, final int line, final int column,
            final String name) {
        super(engine, script, line, column);
        requireValidName(name);
        this.name = name;
    }

    /**
     * Construct a new reference.
     * 
     * @param name
     *            the name of this reference
     */
    public Reference(final String name) {
        super();
        requireValidName(name);
        this.name = name;
    }

    @Override
    public Literal eval(final ScriptContext context) {
        Objects.requireNonNull(context, NULL_CONTEXT);
        return resolve(context).eval(context);
    }

    @Override
    public Type getType(final ScriptContext context) {
        Objects.requireNonNull(context, NULL_CONTEXT);
        return resolve(context).getType(context);
    }

    @Override
    public String toString() {
        return name;
    }

    private Expression resolve(final ScriptContext context) {
        // TODO use custom scope
        // TODO casting
        return (Expression) context.getBindings(ScriptContext.ENGINE_SCOPE).get(name);
    }
}
