package com.company.Sorters;

import com.company.Sorters.Comparer;

import java.awt.*;

/**
 * Created by sky on 5/22/15.
 * Compares colors first based on hue, then by luminosity (brightness).
 */
public class HueComparator implements Comparer<Color> {
    private int shift;

    public HueComparator(int shift) {
        this.shift = shift;
    }

    public HueComparator() {
        this.shift = 0;
    }

    public String getName() {
        return "hueComparator";
    }

    @Override
    public int compare(Color c1, Color c2) {
        float[] hsb1 = new float[3];
        float[] hsb2 = new float[3];
        c1.RGBtoHSB(c1.getRed(), c1.getGreen(), c1.getBlue(), hsb1);
        c2.RGBtoHSB(c2.getRed(), c2.getGreen(), c2.getBlue(), hsb2);
        int c = Float.compare(((hsb1[0] + shift) % 360),((hsb2[0] + shift) % 360));
        if (c == 0) {
            c = Float.compare(hsb1[2], hsb2[2]);
        }
//        if (c == 0) { c = Main.rand.nextInt(11)-5;}
        return c;
    }
}
