package com.company.Sorters;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by sky on 5/23/15.
* Compares colors first based on luminosity (brightness), then by hue.
        */
public class ColorComparator implements Comparer<Color> {

    private int shift = 0;

    private int first  = 0;
    private int second = 1;
    private int third  = 2;

    public ColorComparator(int shift) {
        this.shift = shift;
    }

    public ColorComparator() {}

    private void setOrderFromString(String order) {
        // XXX Kludge to assure order is at least 3 characters long
        order = order + "000";
        int[] numOrder = new int[3];
        for (int i = 0; i < 3; i++) {
            char ch = order.charAt(i);
            switch (ch) {
                case 'r':
                case 'R':
                    numOrder[i] = 0;
                    break;
                case 'g':
                case 'G':
                    numOrder[i] = 1;
                    break;
                case 'b':
                case 'B':
                    numOrder[i] = 2;
                    break;
                default:
                    numOrder[i] = -1;
                    break;
            }
        }
        this.first  = numOrder[0];
        this.second = numOrder[1];
        this.third  = numOrder[2];
    }

    public ColorComparator(String order) {
        setOrderFromString(order);
    }

    public String getName() {
        HashMap<Integer, String> nameMap = new HashMap<>();
        nameMap.put(0, "R");
        nameMap.put(1, "G");
        nameMap.put(2, "B");
        return "colorComp" + nameMap.get(first) + "_" +
                nameMap.get(second) + "_" +
                nameMap.get(third);
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

        int c = Float.compare(c11 / (c12 + c13), c21 / (c22 + c23));
        if (c == 0) {
            c = Float.compare(c12 / c13, c22 / c23);
        }
        return c;
    }
}
