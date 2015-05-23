package com.company;

import java.awt.*;

/**
 * Created by sky on 5/21/15.
 * Base class for all pixel-placing algorithms
 */
public abstract class Algorithm {
    public abstract int count();

    public abstract String getName();

    public void init(int w, int h){
    }

    public void done() {
    }

    public abstract void place (Color c) throws Exception;

    /**
     * Places given color on the image, assumes queue is not empty.
     * @param c
     * @return
     */
    protected abstract Pixel placeImpl(Color c) throws Exception;

    /**
     * Adjusts priority queue after placing a pixel.
     * @param p
     */
    protected abstract void changeQueue(Pixel p);
}
