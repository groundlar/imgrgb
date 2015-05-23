package com.company;

import java.awt.*;
import java.util.*;

/**
 * Created by sky on 5/21/15.
 */
public class AverageNeighborAlgorithm extends Algorithm {
    final int blockSizeLog2 = 2;
    final int blockSize     = 1 << blockSizeLog2;
    final int blockOffset   = 8 - blockSizeLog2;
    final int blockMask     = blockSize - 1;

    int arrayDim = blockSize*blockSize*blockSize;
    ArrayList<Pixel>[] pixelBlocks = new ArrayList[arrayDim];

    boolean[] pixelBlocksVisited = new boolean[arrayDim];

    final int rOffset = blockSize * blockSize;
    final int gOffset = blockSize;
    final int bOffset = 1;

    boolean addedFirst = false;

    @Override
    public int count() {
        return 0;
    }

    @Override
    public String getName() {
        return "avgNhbr";
    }

    @Override
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

    @Override
    public void place(Color c) throws Exception {
        // find next coordinates in image
        Pixel p;
        if (!addedFirst) {
            addedFirst = true;
            // FIXME java is pass by value...
            p = Main.Image[Main.START_Y * Main.WIDTH + Main.START_X];
        } else {
            p = placeImpl(c);
        }

        assert(p.isEmpty);
        p.isEmpty = false;
        p.color = c;

        changeQueue(p);
    }


    @Override
    protected Pixel placeImpl(Color c) throws Exception {
        // Queue
        Queue<Integer> st = new ArrayDeque<Integer>();
        // Stack
        Stack<Integer> undoStack = new DequeStack<>();

        int bestDiff = Integer.MAX_VALUE;
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

            int dr = closest.getRed() - c.getRed();
            int dg = closest.getGreen() - c.getGreen();
            int db = closest.getBlue() - c.getBlue();
//            int diff = Math.abs(dr*dr*dr*dr + dg*dg*dg*dg + db*db*db*db);
            int diff = Math.abs(dr) + Math.abs(dg) + Math.abs(db);
//            diff = Math.max(Math.abs(rDist), Math.max(Math.abs(gDist), Math.abs(bDist)));

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
                int rDist    = avg.getRed() - c.getRed();
                int gDist    = avg.getGreen() - c.getGreen();
                int bDist    = avg.getBlue() - c.getBlue();
                diff = Math.abs(rDist) + Math.abs(gDist) + Math.abs(bDist);
//                diff = Math.abs(rDist*rDist*rDist*rDist + gDist*gDist*gDist*gDist + bDist*bDist*bDist*bDist);
//                diff = Math.max(Math.abs(rDist), Math.max(Math.abs(gDist), Math.abs(bDist)));

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
