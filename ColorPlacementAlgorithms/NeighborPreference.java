package com.company.ColorPlacementAlgorithms;

/**
 * Created by sky on 5/24/15.
 */

public class NeighborPreference {

    public enum preferenceKind {NONE, LINEAR, MULTIPLICATIVE};
    // limit exponent values for speed
    public enum exponent {N_TWO, N_ONE, ZERO, ONE, TWO};

    float coeff;
    exponent expon;
    preferenceKind kind;

    public String getName() {
        String ret = "";
        switch (kind){
            case LINEAR:
                ret += "lin";
                break;
            case MULTIPLICATIVE:
                ret += "mult";
                break;
            default:
        }
        ret = coeff + ret + expon;
        return ret;
    }

    public NeighborPreference(float coeff, exponent expon, preferenceKind kind) {
        this.coeff = coeff;
        this.expon = expon;
        this.kind = kind;
    }

    public double calculate(double dist, double numNeighbors){
        if (kind == preferenceKind.NONE ||
                expon == exponent.ZERO) {
            return dist;
        }

        double ret = dist;
        switch (this.kind) {
            case LINEAR:
                ret = dist - coeff * numNeighbors;
                break;

            case MULTIPLICATIVE:
                switch (expon) {
                    case N_ONE:
                        ret = coeff * dist / numNeighbors;
                        break;
                    case N_TWO:
                        ret = coeff * dist / (numNeighbors*numNeighbors);
                        break;
                    case ONE:
                        ret = coeff * dist * numNeighbors;
                        break;
                    case TWO:
                        ret = coeff * dist * (numNeighbors*numNeighbors);
                        break;
                }
                break;
        }
        return ret;
    }

    // TODO
}
