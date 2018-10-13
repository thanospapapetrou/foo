package com.github.thanospapapetrou.funcky.parser;

import java.io.IOException;
import java.net.URI;

class ParsingErrorException extends ParseException {
    private static final String ERROR_PARSING = "Error parsing %1$s";
    private static final long serialVersionUID = 0L;

    ParsingErrorException(final URI script, final IOException e) {
        super(script, e);
    }

    @Override
    public String getMessage() {
        return String.format(ERROR_PARSING, getFileName());
    }

}
