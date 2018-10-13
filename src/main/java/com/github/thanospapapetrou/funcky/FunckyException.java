package com.github.thanospapapetrou.funcky;

import java.io.IOException;
import java.net.URI;

import javax.script.ScriptException;

/**
 * Exception thrown when a parsing or runtime error occurs.
 * 
 * @author thanos
 */
public abstract class FunckyException extends ScriptException {
    private static final long serialVersionUID = 1L;

    protected FunckyException(final String message, final URI script, final IOException e) {
        super(message, script.toString(), -1);
        initCause(e);
    }

    protected FunckyException(final String message, final URI script, final int line,
            final int column) {
        super(message, script.toString(), line, column);
    }

//    @Override
//    public String getMessage() {
        // TODO find a way to call grandparent method
//        return ((Exception) this).getMessage();
//    }
}
