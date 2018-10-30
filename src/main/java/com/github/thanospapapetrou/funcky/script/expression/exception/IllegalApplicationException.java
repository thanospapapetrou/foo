package com.github.thanospapapetrou.funcky.script.expression.exception;

import com.github.thanospapapetrou.funcky.FunckyEngine;
import com.github.thanospapapetrou.funcky.script.expression.Application;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.Type;

import java.util.Objects;

/**
 * Exception thrown by {@link FunckyEngine} if an illegal application is encountered while checking
 * the type of an expression.
 * 
 * @author thanos
 */
public class IllegalApplicationException extends TypeCheckException {
    private static final String ILLEGAL_APPLICATION =
            "Expression %1$s with type %2$s can not be applied to expression %3$s with type %4$s";
    private static final String NULL_APPLICATION = "Application must not be null";
    private static final String NULL_ARGUMENT_TYPE = "Argument type must not be null";
    private static final String NULL_FUNCTION_TYPE = "Function type must not be null";

    private static final long serialVersionUID = 0L;

    /**
     * Construct a new illegal application exception.
     * 
     * @param application
     *            the illegal application that caused this illegal application exception
     * @param functionType
     *            the type of the function of the illegal application
     * @param argumentType
     *            the type of the argument of the illegal application
     */
    public IllegalApplicationException(final Application application, final Type functionType,
            final Type argumentType) {
        super(String.format(ILLEGAL_APPLICATION,
                Objects.requireNonNull(application, NULL_APPLICATION).getFunction(),
                Objects.requireNonNull(functionType, NULL_FUNCTION_TYPE), application.getArgument(),
                Objects.requireNonNull(argumentType, NULL_ARGUMENT_TYPE)), application.getScript(),
                application.getLine(), application.getColumn());
    }

}
