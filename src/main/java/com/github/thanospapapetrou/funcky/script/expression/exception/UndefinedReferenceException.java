package com.github.thanospapapetrou.funcky.script.expression.exception;

import com.github.thanospapapetrou.funcky.FunckyEngine;
import com.github.thanospapapetrou.funcky.script.expression.Reference;

import java.util.Objects;

/**
 * Exception thrown by {@link FunckyEngine} if an undefined reference is encountered while checking
 * the type of an expression.
 * 
 * @author thanos
 */
public class UndefinedReferenceException extends TypeCheckException {
    private static final String NULL_REFERENCE = "Reference must not be null";
    private static final String UNDEFINED_REFERENCE = "Reference %1$s is undefined";
    private static final long serialVersionUID = 0L;

    /**
     * Construct a new undefined reference exception.
     * 
     * @param reference
     *            the undefined reference encountered
     */
    public UndefinedReferenceException(final Reference reference) {
        super(String.format(UNDEFINED_REFERENCE, Objects.requireNonNull(reference, NULL_REFERENCE)),
                reference.getScript(), reference.getLine(), reference.getColumn());
    }
}
