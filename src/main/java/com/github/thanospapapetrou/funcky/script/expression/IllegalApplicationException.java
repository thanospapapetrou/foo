package com.github.thanospapapetrou.funcky.script.expression;

import com.github.thanospapapetrou.funcky.FunckyException;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.Type;

class IllegalApplicationException extends FunckyException {
    private static final String ILLEGAL_APPLICATION =
            "Expression %1$s with type %2$s can not be applied to expression %3$s with type %4$s";
    private static final long serialVersionUID = 0L;

    public IllegalApplicationException(final Application application, final Type functionType,
            final Type argumentType) {
        super(String.format(ILLEGAL_APPLICATION, application.getFunction(), functionType,
                application.getArgument(), argumentType), application.getScript(),
                application.getLine(), application.getColumn());
    }

}
