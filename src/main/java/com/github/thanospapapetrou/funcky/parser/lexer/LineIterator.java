package com.github.thanospapapetrou.funcky.parser.lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;

class LineIterator implements Iterator<String> {
    private final BufferedReader reader;
    private final URI file;
    private String next;

    LineIterator(final BufferedReader reader, final URI file) {
        this.reader = reader;
        this.file = file;
        try {
            this.next = reader.readLine();
        } catch (final IOException e) {
            throw new LineIteratorException(file, e);
        }
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public String next() {
        final String next = this.next;
        try {
            this.next = reader.readLine();
        } catch (final IOException e) {
            throw new LineIteratorException(file, e);
        }
        return next;
    }
}
