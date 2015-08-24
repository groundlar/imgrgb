package com.company.ColorPlacementAlgorithms;

import com.company.Main;
import com.company.Pixel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sky on 5/21/15.
 * Base class for all pixel-placing algorithms
 */
public abstract class Algorithm {
    // Blocks for preliminary coarse color search
    static Random rand = new Random(Main.SEED);
    final int blockSizeLog2 = 2;
    final int blockSize     = 1 << blockSizeLog2;
    final int blockOffset   = 8 - blockSizeLog2;
    final int blockMask     = blockSize - 1;

    int arrayDim = blockSize*blockSize*blockSize;
    ArrayList<Pixel>[] pixelBlocks = new ArrayList[arrayDim];

    final int rOffset = blockSize * blockSize;
    final int gOffset = blockSize;
    final int bOffset = 1;

    final int SEED_NUM = 2;
    final int MULT_SEED_DELAY = (Main.HEIGHT * Main.WIDTH) / 5;

    boolean[] pixelBlocksVisited = new boolean[arrayDim];
    static long pixelsAdded = 0;

    public String getName() {
        return SEED_NUM + "seeds" + String.format("%05d", MULT_SEED_DELAY) + "d";
    };

    public void init(int width, int height){
        for (int r = 0; r < blockSize; r++) {
            for (int g = 0; g < blockSize; g++) {
                for (int b = 0; b < blockSize; b++) {
                    pixelBlocks[r * blockSize*blockSize +
                            g * blockSize +
                            b] = new ArrayList<Pixel>();
                }
            }
        }
    }

    public void done() {
    }

    public void place(Color c) throws Exception {
        // find next coordinates in image
        Pixel p;
        if (pixelsAdded==0) {
            p = Main.Image[Main.START_Y * Main.WIDTH + Main.START_X];
        } else if (pixelsAdded==MULT_SEED_DELAY){
            p = Main.Image[(Main.START_Y + Main.HEIGHT/2)* Main.WIDTH + Main.START_X + Main.WIDTH/3];
        } else {
            p = placeImpl(c);
        }

        assert(p.isEmpty);
        p.isEmpty = false;
        p.color = c;

        pixelsAdded++;
        changeQueue(p);
    }

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
