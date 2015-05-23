package com.company;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by sky on 5/21/15.
 */
public class GenericPixelQueue<T extends Object> extends PixelQueue {
    public T[] data;
    public GenericPixelQueue(Class<T> c) {
        // Use Array native method to create array
        // of a type only known at run time
        @SuppressWarnings("unchecked")
        T[] data = (T[]) Array.newInstance(c, pixels.length);
        this.data = data;
    }

    @Override
    public void add(Pixel p) {
        super.add(p);
        // maintain identical array sizes
        if (pixels.length != data.length){
            data = Arrays.copyOf(data, pixels.length);
        }
    }

    @Override
    public void readd(Pixel p){
        // Maintain data when moving Pixel
        T pxData = data[p.queueIndex];
        super.readd(p);
        data[p.queueIndex] = pxData;
    }
}
