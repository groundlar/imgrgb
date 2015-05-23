package com.company;

/**
 * Created by sky on 5/21/15.
 * Stack interface, modeling a LIFO stack.
 */
public interface Stack<T> {
    void push(T object);
    T pop();
    int size();
}
