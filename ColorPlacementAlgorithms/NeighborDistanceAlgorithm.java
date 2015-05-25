package com.company.ColorPlacementAlgorithms;

import com.company.*;

import java.awt.*;
import java.util.*;
/**
 * Created by sky on 5/23/15.
 */
public class NeighborDistanceAlgorithm extends WeightedDistPlacementAlgorithm {
    public enum neighborMetric {MIN, AVG};
    neighborMetric nghbr;

    public NeighborDistanceAlgorithm(ColorMetric distMetric, NeighborPreference neighborWeight, neighborMetric nghbr) {
        super(distMetric, neighborWeight);
        this.nghbr = nghbr;
    }


    @Override
    public String getName() { return "nhbrDist"; }

    // TODO how to calculate minimum-difference pixels?
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
