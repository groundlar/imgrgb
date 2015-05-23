package com.company;

import java.util.AbstractCollection;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * Created by sky on 5/21/15.
 * Implements a stack using an ArrayDeque. Useful for information
 * hiding to prevent abuse of a Deque intended as a Stack.
 */
public class DequeStack<T> extends AbstractCollection<T> implements Stack<T> {

    private final Deque<T> deque = new ArrayDeque<T>();

    @Override
    public void push(T object) {
        deque.addFirst(object);
    }

    @Override
    public T pop() {
        return deque.removeFirst();
    }

    @Override
    public Iterator<T> iterator() {
        return deque.iterator();
    }

    @Override
    public int size() {
        return deque.size();
    }
}