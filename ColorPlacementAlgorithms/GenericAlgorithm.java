package com.company.ColorPlacementAlgorithms;

import com.company.*;

import java.awt.*;
import java.util.*;

/**
 * Created by sky on 5/21/15.
 */
public class GenericAlgorithm extends Algorithm {
    final static boolean NEG_WEIGHTING = true;
    final static boolean CENTER_WEIGHTING = false;
    final static boolean ANTI_CENTER_WEIGHTING = false;
    final static int LOC_SCALING = 128; // Empirical constant

    ColorMetric distFunction; //= new ColorDistanceMetrics.cosineSqRGBDist();
//    NeighborPreference pref;

    public GenericAlgorithm(ColorMetric metric){//, NeighborPreference pref) {
        this.distFunction = metric;
//        this.pref = pref;
    }

    @Override
    public String getName() {
        String ret = distFunction.getName();
        if (NEG_WEIGHTING) ret += "-LnQd";
        if (CENTER_WEIGHTING) ret += "-Cent" + LOC_SCALING;
        if (ANTI_CENTER_WEIGHTING) ret += "-Dispersed" + LOC_SCALING;
        return ret + super.getName();
    }

    @Override
    protected Pixel placeImpl(Color c) throws Exception {
        // Queue
        Queue<Integer> st = new ArrayDeque<>();
        // Stack
        com.company.Stack<Integer> undoStack = new DequeStack<>();

        int bestDiff = Integer.MAX_VALUE;
        float lastShift = 0;
        Pixel bestPixel = null;
        int bestBlock = -1;

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

            int diff = (int)distFunction.dist(closest, c);

            if (diff > bestDiff) continue;

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
                diff = (int)distFunction.dist(avg, c);

                lastShift = 0;
                if (NEG_WEIGHTING){
                    // max of 64 : 8 possible neighbors
                    lastShift -= p.nonEmptyNeigh*p.nonEmptyNeigh;
                }
                if (CENTER_WEIGHTING){
                    // max of WIDTH + HEIGHT - xPos - yPos
                    int maxDist = 0;
                    if (p.xPos < (Main.WIDTH / 2)) {
                        maxDist += Main.WIDTH - p.xPos;
                    } else {
                        maxDist += p.xPos;
                    }
                    if (p.yPos < (Main.HEIGHT / 2)) {
                        maxDist += Main.HEIGHT - p.yPos;
                    } else {
                        maxDist += p.yPos;
                    }
                    float shift = Math.abs(p.xPos - Main.START_X) + Math.abs(p.yPos - Main.START_Y);
                    lastShift += (shift / maxDist) * LOC_SCALING;
                }
                if (ANTI_CENTER_WEIGHTING){
                    int maxDist = 0;
                    if (Main.START_X < (Main.WIDTH / 2)) {
                        maxDist += Main.WIDTH - Main.START_X;
                    } else {
                        maxDist += Main.START_X;
                    }
                    if (Main.START_Y < (Main.HEIGHT / 2)) {
                        maxDist += Main.HEIGHT - Main.START_Y;
                    } else {
                        maxDist += Main.START_Y;
                    }
                    float shift = Math.abs(p.xPos - Main.START_X) + Math.abs(p.yPos - Main.START_Y);
                    lastShift -= (shift / maxDist) * LOC_SCALING;
                }
                diff += lastShift;

                // This has to wait on lambda functions, I can't come up with a fast enough method otherwise
//                diff = (int)pref.calculate(diff, p.nonEmptyNeigh);






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

        if (bestDiff == Integer.MAX_VALUE){
            throw new Exception("No possible positions!");
        }

        if (!pixelBlocks[bestBlock].remove(bestPixel)){
            throw new Exception("Could not remove pixel " + bestPixel + " from " + bestBlock + " !");
        }

        bestPixel.inQueue = false;

        return bestPixel;
    }

    @Override
    protected void changeQueue(Pixel p) {
        // recalculate neighbors
        for (Pixel np : p.neighbors) {
            if (np.isEmpty){
                int r = 0, g = 0, b = 0, n = 0;
                for (Pixel nnp : np.neighbors) {
                    if (!nnp.isEmpty){
                        r += nnp.color.getRed();
                        g += nnp.color.getGreen();
                        b += nnp.color.getBlue();
                        n++;
                    }
                }

                np.nonEmptyNeigh++;

                r /= n;
                g /= n;
                b /= n;

                Color avg = new Color(r, g, b);

                Color newBlock = new Color(r >> blockOffset & blockMask,
                        g >> blockOffset & blockMask,
                        b >> blockOffset & blockMask);
                int blockIndex = newBlock.getRed() * rOffset +
                        newBlock.getGreen() * gOffset +
                        newBlock.getBlue() * bOffset;

                if (!np.inQueue) {
                    np.block   = blockIndex;
                    np.inQueue = true;
                    pixelBlocks[blockIndex].add(np);
                } else if (blockIndex != np.block) {
                    if (!pixelBlocks[np.block].remove(np)) {
                        System.out.println("Couldn't remove pixel " + np + ", even though it should be in block " + blockIndex + " !");
                    }

                    np.inQueue = true;
                    np.block = blockIndex;
                    pixelBlocks[blockIndex].add(np);
                }

                np.avg = avg;

            }
        }
    }
}

