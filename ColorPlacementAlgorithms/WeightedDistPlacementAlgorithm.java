package com.company.ColorPlacementAlgorithms;

import com.company.*;

import java.awt.*;
import java.util.*;

/**
 * Created by sky on 5/24/15.
 */
public abstract class WeightedDistPlacementAlgorithm extends Algorithm {

    ColorMetric distMetric;
    NeighborPreference neighborWeight;

    public WeightedDistPlacementAlgorithm(ColorMetric distMetric, NeighborPreference neighborWeight) {
        this.distMetric = distMetric;
        this.neighborWeight = neighborWeight;
    }

    @Override
    protected Pixel placeImpl(Color c) throws Exception {
        // Queue
        Queue<Integer> st = new ArrayDeque<Integer>();
        // Stack
        com.company.Stack<Integer> undoStack = new DequeStack<>();

        double bestDiff = Double.MAX_VALUE;
        Pixel bestPixel = null;
        int bestBlock = -1;

        int count = 0;


        // Truncate color channels to the lowest log2(blockMask) digits
        // These digits are shared by all colors in the current block
        Color rounded = new Color(
                (c.getRed() >> blockOffset) & blockMask,
                (c.getGreen() >> blockOffset) & blockMask,
                (c.getBlue() >> blockOffset) & blockMask);

        st.clear();
        st.add(rounded.getRed() * rOffset +
                rounded.getGreen() * gOffset +
                rounded.getBlue() * bOffset);

        while (st.size() > 0) {
            count++;
            int coord = st.remove();
            undoStack.push(coord);

            // Fetch the relevant parts of the reduced int
            // corresponding to each color channel
            byte r = (byte)((coord >> (blockSizeLog2*2)) & blockMask);
            byte g = (byte)((coord >> (blockSizeLog2*1)) & blockMask);
            byte b = (byte)((coord >> (blockSizeLog2*0)) & blockMask);
            Color expanded = new Color(r, g, b);

            // Minimum color channel value in the
            // currently-searched color block
            Color min = new Color(r << blockOffset,
                    g << blockOffset,
                    b << blockOffset);

            // Maximum color channel value in the
            // currently-searched color block
            Color max = new Color((r << blockOffset) + blockMask,
                    (g << blockOffset) + blockMask,
                    (b << blockOffset) + blockMask);

            int closestR = (c.getRed() < min.getRed()) ?
                    min.getRed() :
                    (c.getRed() > max.getRed() ?
                            max.getRed() : c.getRed());
            int closestG = (c.getGreen() < min.getGreen()) ?
                    min.getGreen() :
                    (c.getGreen() > max.getGreen() ?
                            max.getGreen() : c.getGreen());
            int closestB = (c.getBlue() < min.getBlue()) ?
                    min.getBlue() :
                    (c.getBlue() > max.getBlue() ?
                            max.getBlue() : c.getBlue());

            Color closest = new Color(closestR, closestG, closestB);

            double diff = this.distMetric.dist(closest, c);

            if (diff > bestDiff) continue;

            // TODO comment these more coherently
            // If color channel is greater than zero and haven't
            // visited the left-bordering color block,
            // then add the block to queue
            if (expanded.getRed() > 0 && !(pixelBlocksVisited[coord - rOffset])) {
                pixelBlocksVisited[coord - rOffset] = true;
                st.add(coord - rOffset);
            }
            if (expanded.getGreen() > 0 && !(pixelBlocksVisited[coord - gOffset])) {
                pixelBlocksVisited[coord - gOffset] = true;
                st.add(coord - gOffset);
            }
            if (expanded.getBlue() > 0 && !(pixelBlocksVisited[coord - bOffset])) {
                pixelBlocksVisited[coord - bOffset] = true;
                st.add(coord - bOffset);
            }

            // TODO comment these more coherently
            // If color channel is less than blockMask and haven't
            // visited the right-bordering color block,
            // then add the block to queue
            if (expanded.getRed() < blockMask && !(pixelBlocksVisited[coord + rOffset])) {
                pixelBlocksVisited[coord + rOffset] = true;
                st.add(coord + rOffset);
            }
            if (expanded.getGreen() < blockMask && !(pixelBlocksVisited[coord + gOffset])) {
                pixelBlocksVisited[coord + gOffset] = true;
                st.add(coord + gOffset);
            }
            if (expanded.getBlue() < blockMask && !(pixelBlocksVisited[coord + bOffset])) {
                pixelBlocksVisited[coord + bOffset] = true;
                st.add(coord + bOffset);
            }


            // Iterate over pixels in block
            ArrayList<Pixel> pxl = pixelBlocks[coord];
            for (Pixel p : pxl){
                Color avg = p.avg;
                diff = this.distMetric.dist(avg, c);

                diff = this.neighborWeight.calculate(diff, p.nonEmptyNeigh);

                if (diff < bestDiff || (diff == bestDiff && p.weight < bestPixel.weight)){
                    bestDiff  = diff;
                    bestPixel = p;
                    bestBlock = coord;
                }
            }
        }

        // Mark blocks as not visited for the subsequent iteration
        while (undoStack.size() > 0){
            int coord = undoStack.pop();
            pixelBlocksVisited[coord] = false;
        }

        if (bestDiff == Double.MAX_VALUE){
            throw new Exception("No possible positions!");
        }

        if (!pixelBlocks[bestBlock].remove(bestPixel)){
            throw new Exception("Could not remove pixel " + bestPixel + " from " + bestBlock + " !");
        }

        bestPixel.inQueue = false;

        return bestPixel;
    }

}
