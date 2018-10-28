package com.github.thanospapapetrou.funcky.script.expression;

import com.github.thanospapapetrou.funcky.FunckyEngine;
import com.github.thanospapapetrou.funcky.script.expression.literal.Function;
import com.github.thanospapapetrou.funcky.script.expression.literal.Literal;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.FunctionType;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.Type;

import java.net.URI;
import java.util.Objects;

import javax.script.ScriptContext;

/**
 * Class representing an application.
 * 
 * @author thanos
 */
public class Application extends Expression {
    private static final String APPLICATION_FORMAT = "%1$s %2$s";
    private static final String NESTED_APPLICATION = "(%1$s)";
    private static final String NULL_CONTEXT = "Context must not be null";

    private final Expression function;
    private final Expression argument;

    /**
     * Construct a new application.
     * 
     * @param engine
     *            the engine that compiled this application
     * @param script
     *            the URI of the script in which this application was encountered
     * @param line
     *            the line of the script at which this application was encountered
     * @param column
     *            the column of the script at which this application was encountered
     * @param function
     *            the function of this application
     * @param argument
     *            the argument of this application
     */
    public Application(final FunckyEngine engine, final URI script, final int line,
            final int column, final Expression function, final Expression argument) {
        super(engine, script, line, column);
        this.function = function;
        this.argument = argument;
    }

    /**
     * Construct a new application.
     * 
     * @param function
     *            the function of this application
     * @param argument
     *            the argument of this application
     */
    public Application(final Expression function, final Expression argument) {
        super();
        this.function = function;
        this.argument = argument;
    }

    @Override
    public Literal eval(final ScriptContext context) {
        Objects.requireNonNull(context, NULL_CONTEXT);
        // TODO function may not be a function
        // TODO casting
        return ((Function) function.eval(context)).apply(context, argument);
    }

    @Override
    public Type getType(final ScriptContext context) {
        Objects.requireNonNull(context, NULL_CONTEXT);
        // TODO casting
        return ((FunctionType) function.getType(context)).getRange();
    }

    @Override
    public String toString() {
        return String.format(APPLICATION_FORMAT, function, (argument instanceof Application)
                ? String.format(NESTED_APPLICATION, argument) : argument);
    }
}
