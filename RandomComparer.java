package com.company;

import java.awt.*;
import java.util.Comparator;

/**
 * Created by sky on 5/22/15.
 */
public class RandomComparer implements Comparator<Color>{
    @Override
    public int compare(Color c1, Color c2) {
        return Main.rand.nextInt(11) - 5;
    }
}
