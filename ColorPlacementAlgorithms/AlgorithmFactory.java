package com.company.ColorPlacementAlgorithms;

import com.company.ColorPlacementAlgorithms.Algorithm;

/**
 * Created by sky on 5/24/15.
 */
public class AlgorithmFactory {
    public enum distanceMetric {RGBChebychev, cosineSqRGBDist,
        HSBChebychev, cosineSqHSBDist,
        sumRGBDist, sumSqRGBDist, sumCubeRGBDist, sumQrtRGBDist,
        sumHSBDist, sumSqHSBDist, sumCubeHSBDist, sumQrtHSBDist};


    public static Algorithm getAlgorithm(distanceMetric type,
                                         NeighborDistanceAlgorithm.neighborMetric nghbr,
                                         NeighborPreference weighting) {

        ColorMetric distMetric;
        switch (type) {
            // TODO
            case RGBChebychev:
                distMetric = new ColorDistanceMetrics.RGBChebychev();
                break;
            case cosineSqRGBDist:
                distMetric = new ColorDistanceMetrics.cosineSqRGBDist();
                break;

            case HSBChebychev:
                distMetric = new ColorDistanceMetrics.HSBChebychev();
                break;
            case cosineSqHSBDist:
                distMetric = new ColorDistanceMetrics.cosineSqHSBDist();
                break;

            case sumRGBDist:
                distMetric = new ColorDistanceMetrics.sumRGBDist();
                break;
            case sumSqRGBDist:
                distMetric = new ColorDistanceMetrics.sumSqRGBDist();
                break;
            case sumCubeRGBDist:
                distMetric = new ColorDistanceMetrics.sumCubeRGBDist();
                break;
            case sumQrtRGBDist:
                distMetric = new ColorDistanceMetrics.sumQrtRGBDist();
                break;


            case sumHSBDist:
                distMetric = new ColorDistanceMetrics.sumHSBDist();
                break;
            case sumSqHSBDist:
                distMetric = new ColorDistanceMetrics.sumSqHSBDist();
                break;
            case sumCubeHSBDist:
                distMetric = new ColorDistanceMetrics.sumCubeHSBDist();
                break;
            case sumQrtHSBDist:
                distMetric = new ColorDistanceMetrics.sumQrtHSBDist();
                break;

            default:
                return null;
        }


        return new NeighborDistanceAlgorithm(distMetric, weighting, nghbr);
    }
}
