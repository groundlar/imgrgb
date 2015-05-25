package com.company.ColorPlacementAlgorithms;

/**
 * Created by sky on 5/24/15.
 */

public class NeighborPreference {

    public enum preferenceKind {NONE, LINEAR, MULTIPLICATIVE};

    float coeff;
    float expon;
    preferenceKind kind;


    public NeighborPreference(float coeff, float expon, preferenceKind kind) {
        this.coeff = coeff;
        this.expon = expon;
        this.kind = kind;
    }

    public double calculate(double dist, double numNeighbors){
        double ret;
        switch (this.kind) {
            case NONE:
                ret = dist;
                break;


            case LINEAR:
                numNeighbors = Math.pow(numNeighbors, coeff);
                numNeighbors *= coeff;
                ret = dist - numNeighbors;
                break;

            case MULTIPLICATIVE:
                numNeighbors = Math.pow(numNeighbors, coeff);
                numNeighbors *= coeff;
                ret = dist * numNeighbors;
                break;

            default:
                ret = dist;
        }
        return ret;
    }

    // TODO
}
