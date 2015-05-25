package com.company.Sorters;

import java.awt.*;

/**
 * Created by sky on 5/22/15.
 * Compares colors first based on luminosity (brightness), then by hue.
 */
public class BrightnessComparator implements Comparer<Color> {

    private int shift;

    public BrightnessComparator(int shift) {
        this.shift = shift;
    }

    public BrightnessComparator() {
        this.shift = 0;
    }

    public String getName() {
        return "brightnessComparator";
    }

    @Override
    public int compare(Color c1, Color c2) {
        float[] hsb1 = new float[3];
        float[] hsb2 = new float[3];
        c1.RGBtoHSB(c1.getRed(), c1.getGreen(), c1.getBlue(), hsb1);
        c2.RGBtoHSB(c2.getRed(), c2.getGreen(), c2.getBlue(), hsb2);
        int c = Float.compare(hsb2[2], hsb1[2]);
        if (c == 0) {
            c = Float.compare(((hsb2[0] + shift) % 360),((hsb1[0] + shift) % 360));
        }
//        if (c == 0) { c = Main.rand.nextInt(11)-5;}
        return c;
    }
}
