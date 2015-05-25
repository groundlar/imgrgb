package com.company.Sorters;

import java.util.Comparator;

/**
 * Created by sky on 5/22/15.
 * Dummy class to add name to Comparator interface,
 * used in output filename generation.
 */
public interface Comparer<T> extends Comparator<T> {
    String getName();
}
