package com.github.thanospapapetrou.funcky.parser.tokenizer;

import com.github.thanospapapetrou.funcky.parser.tokenizer.exception.ReadingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;

/**
 * Class implementing an iterator over the lines of a {@link BufferedReader}.
 * 
 * @author thanos
 */
public class LineIterator implements Iterator<String> {
    private final URI script;
    private final BufferedReader reader;
    private String next;

    LineIterator(final URI script, final BufferedReader reader) throws IOException {
        this.script = script;
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
            throw new ReadingException(script, e);
        }
        return next;
    }
}
