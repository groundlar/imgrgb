package com.company;

import java.util.Arrays;

/**
 * Created by sky on 5/21/15.
 * Priority pixel queue, supports O(1) lookups, addition, and removal.
 */
public class PixelQueue {
    // Array of pixels, some can be null
    public Pixel[] pixels;

    // Number of entries in queue
    public int count = 0;

    // First unused index (where we can add to)
    public int usedUntil;


    public PixelQueue() {
        pixels = new Pixel[1024];
    }

    /**
     * Add pixel to queue
     * @param p
     */
    public void add(Pixel p) {
        assert(p.queueIndex == -1);
        if (usedUntil == pixels.length) {
            pixels = Arrays.copyOf(pixels, pixels.length*2);
        }
        pixels[usedUntil] = p;
        p.queueIndex = usedUntil;
        usedUntil++;
        count++;
    }

    /**
     * Removes a pixel from the queue
     * @param p
     */
    public void remove(Pixel p){
        assert(p.queueIndex > -1);
        pixels[p.queueIndex] = null;
        p.queueIndex = -1;
        count--;
    }

    /**
     * Reads a pixel, while maintaining its associated data.
     * Primarily used for restructuring / compressing array.
     * @param p
     */
    public void readd(Pixel p){
        remove(p);
        add(p);
    }


    /**
     * Compresses array by moving elements to front.
     */
    public void compress() {
        if ((double)usedUntil / count < 1.05){
            return; // Allow up to 5% of space wasted
        }

        usedUntil = 0;
        for (int i = 0; usedUntil < count; i++){
            if (pixels[i] != null){
                readd(pixels[i]);
            }
        }
    }
}
