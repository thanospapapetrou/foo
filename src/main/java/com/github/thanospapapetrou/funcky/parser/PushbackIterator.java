package com.github.thanospapapetrou.funcky.parser;

import java.util.Iterator;
import java.util.Stack;

class PushbackIterator<E> implements Iterator<E> {
    private final Iterator<E> iterator;
    private final Stack<E> items;

    PushbackIterator(final Iterator<E> iterator) {
        this.iterator = iterator;
        this.items = new Stack<>();
    }

    void pushback(final E item) {
        items.push(item);
    }

    @Override
    public boolean hasNext() {
        return (!items.isEmpty()) || iterator.hasNext();
    }

    @Override
    public E next() {
        return items.isEmpty() ? iterator.next() : items.pop();
    }

    @Override
    public void remove() {
        if (items.isEmpty()) {
            iterator.remove();
        } else {
            items.pop();
        }
    }
}
