package com.company.Sorters;

import com.company.Sorters.Comparer;

import java.awt.*;

/**
 * Created by sky on 5/22/15.
 * Compares colors first based on hue, then by luminosity (brightness).
 */
public class HueComparator implements Comparer<Color> {
    private int shift;

    /**
     * Initializes a HueComparator, using an integer shift for sorting, allowing arbitrary
     * definition of the value 0 color.
     * @param shift
     */
    public HueComparator(int shift) {
        this.shift = shift;
    }

    public HueComparator() {
        this.shift = 0;
    }

    public String getName() {
        return "HSBComp";
    }

    /**
     * Compares two colors according to the initialized order during
     * E.g. assuming initialization with 'RGB', the values are first sorted by
     * hue, then saturation, then brightness, in the event of primary value
     * collisions.
     *
     * @param c1
     * @param c2
     * @return -1, 0, or 1
     */
    @Override
    public int compare(Color c1, Color c2) {
        float[] hsb1 = new float[3];
        float[] hsb2 = new float[3];
        c1.RGBtoHSB(c1.getRed(), c1.getGreen(), c1.getBlue(), hsb1);
        c2.RGBtoHSB(c2.getRed(), c2.getGreen(), c2.getBlue(), hsb2);
        // First sort by hue
        int c = Float.compare(((hsb1[0] + shift) % 360),((hsb2[0] + shift) % 360));
        if (c == 0) {
            // Then by saturation
            c = Float.compare(hsb1[1], hsb2[1]);
        }
        if (c == 0) {
            // Then by brightness
            c = Float.compare(hsb1[2], hsb2[2]);
        }
        return c;
    }
}
