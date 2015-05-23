package com.company;

import java.awt.*;

/**
 * Created by sky on 5/21/15.
 * Represents a pixel in an image
 */
public class Pixel {
    /* Is this pixel empty? */
    public boolean isEmpty;
    /* Is this pixel stored in the queue? */
    public boolean inQueue;

    public byte nonEmptyNeigh = 0;
    public int block;

    public Color avg;

    // Color of this pixel
    public Color color;

    // Index of this pixel in queue
    // TODO
    public int queueIndex;

    // Precalculated array of neighbor pixels
    public Pixel[] neighbors;

    // Unique weight used for tiebreaking comparisons
    public int weight;


    public Pixel(boolean isEmpty, boolean inQueue, int weight, int queueIndex) {
        this.isEmpty = isEmpty;
        this.inQueue = inQueue;
        this.weight = weight;
        this.queueIndex = queueIndex;
    }
}
