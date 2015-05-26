package com.company.Sorters;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by sky on 5/23/15.
 * Compares colors first based on luminosity (brightness), then by hue.
 */
public class StrictColorComparator extends ColorComparator {

    private int shift = 0;

    private int first  = 0;
    private int second = 1;
    private int third  = 2;


    public StrictColorComparator(String order) {
        super(order);
    }

    public String getName() {
        return "strt" + super.getName();
    }

    @Override
    public int compare(Color c1, Color c2) {
        int[] c1RGB = {c1.getRed(), c1.getGreen(), c1.getBlue()};
        int[] c2RGB = {c2.getRed(), c2.getGreen(), c2.getBlue()};

        float c11, c12, c13;
        float c21, c22, c23;

        if (this.first != -1) {
            c11 = c1RGB[first];
            c21 = c2RGB[first];
        } else {
            c11 = 1;
            c21 = 1;
        }

        if (this.second != -1) {
            c12 = c1RGB[second];
            c22 = c2RGB[second];
        } else {
            c12 = 1;
            c22 = 1;
        }
        if (this.third != -1) {
            c13 = c1RGB[third];
            c23 = c2RGB[third];
        } else {
            c13 = 1;
            c23 = 1;
        }

        // TODO why is sorting independent of second and third order?
        int c = Float.compare(c11 / (c12 + c13),
                              c21 / (c22 + c23));
        if (c == 0) {
            c = Float.compare(c11, c21);
        }
        if (c == 0) {
            c = Float.compare(c12 / c13,
                              c22 / c23);
        }
        return c;
    }
}
