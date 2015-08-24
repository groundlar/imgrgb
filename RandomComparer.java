package com.company;

import com.company.Sorters.Comparer;

import java.awt.*;
import java.util.Comparator;

/**
 * Created by sky on 5/22/15.
 */
public class RandomComparer implements Comparer<Color> {
    @Override
    public int compare(Color c1, Color c2) {
        return Main.rand.nextInt(11) - 5;
    }

    @Override
    public String getName() {
        return "randComp";
    }
}
