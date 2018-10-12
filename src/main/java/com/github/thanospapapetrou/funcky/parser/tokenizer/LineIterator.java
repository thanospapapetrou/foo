package com.github.thanospapapetrou.funcky.parser.tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

class LineIterator implements Iterator<String> {
    private final BufferedReader reader;
    private String next;

    LineIterator(final BufferedReader reader) throws IOException {
        this.reader = reader;
        this.next = reader.readLine();
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
            throw new ReadingException(e);
        }
        return next;
    }
}
