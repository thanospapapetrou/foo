package com.github.thanospapapetrou.funcky.script.expression;

import com.github.thanospapapetrou.funcky.FunckyException;

class UndefinedReferenceException extends FunckyException {
    private static final String UNDEFINED_REFERENCE = "Reference %1$s is undefined";
    private static final long serialVersionUID = 0L;

    UndefinedReferenceException(final Reference reference) {
        super(String.format(UNDEFINED_REFERENCE, reference), reference.getScript(),
                reference.getLine(), reference.getColumn());
    }
}
