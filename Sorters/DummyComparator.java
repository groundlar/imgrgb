package com.company.Sorters;

import com.company.Sorters.Comparer;

import java.awt.*;

/**
 * Created by sky on 5/23/15.
 * Compares colors first based on luminosity (brightness), then by hue.
 */
public class DummyComparator implements Comparer<Color> {

    public DummyComparator() {}

    public String getName() {
        return "dummyComp";
    }

    @Override
    public int compare(Color c1, Color c2) {
        return 0;
    }
}
