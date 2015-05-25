package com.company.ColorPlacementAlgorithms;

import java.awt.*;

/**
 * Created by sky on 5/24/15.
 */
public abstract class ColorMetric {
        abstract double dist(Color c1, Color c2);

        public String getName(){
              return this.getClass().getName();
        }
}
