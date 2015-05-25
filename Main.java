package com.company;

import com.company.ColorPlacementAlgorithms.*;
import com.company.Sorters.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class Main {
    // Width and height of the image
    /** Common numbers of colors and dimensions:
     *  256 : 4096x4096
     *  192 : 3456x2048
     *  128 : 2048 x 1024
     *  64 : 512 x 512
     */
    final static int NUM_COLORS = 64;
    final static int MAX_COLORS = 256;
    public final static int WIDTH  = 512;
    final static int HEIGHT = 512;
    // Starting coordinates for first pixel
    public final static int START_X = WIDTH / 2 - 1;
    public final static int START_Y = HEIGHT / 2 - 1;
    final static int SEED = 109103113;//new Random().nextInt();

    private static NeighborDistanceAlgorithm.neighborMetric ngbhrMetric =
            NeighborDistanceAlgorithm.neighborMetric.MIN;
    private static AlgorithmFactory.distanceMetric dist =
            AlgorithmFactory.distanceMetric.sumRGBDist;
    private static NeighborPreference ngbhrPref =
            new NeighborPreference(1, -2, NeighborPreference.preferenceKind.MULTIPLICATIVE);

    /*** End configuration parameters ***/

    public static Pixel[] Image;
    static Random rand = new Random(SEED);
    private static final int[] NEIGH_X = {-1, 0, 1, -1, 1, -1, 0, 1};
    private static final int[] NEIGH_Y = {-1, -1, -1, 0, 0, 1, 1, 1};

    private static Algorithm ALGORITHM = AlgorithmFactory.getAlgorithm(dist, ngbhrMetric, ngbhrPref);
    private static Comparer SORTER = new BrightnessComparator();

    public static void main(String[] args) {
        // TODO cmd-line args

        System.out.println("Generating Colors...");
        Color colors[] = new Color[WIDTH * HEIGHT];
        for (int r = 0; r < NUM_COLORS; r++) {
            int R = (r*255 / (NUM_COLORS - 1));
            for (int g = 0; g < NUM_COLORS; g++) {
                int G = (g*255 / (NUM_COLORS - 1));
                for (int b = 0; b < NUM_COLORS; b++) {
                    int B = (b*255 / (NUM_COLORS - 1));
                    int ind = r*NUM_COLORS*NUM_COLORS + g*NUM_COLORS + b;
                    colors[ind] = new Color(R, G, B);
                }
            }
        }

        System.out.println("Sorting...");
        // TODO Implement different sorting algos, learn java sorting syntax
        Arrays.sort(colors, SORTER);
        assert(colors.length == WIDTH * HEIGHT);

        System.out.println("Randomizing weights...");
        Image = new Pixel[WIDTH * HEIGHT];

        int dupCount = 0;
        HashSet<Integer> weights = new HashSet<>();
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int weight = -1;
                do {
                    if (weight != -1) dupCount++;
                    weight = rand.nextInt(Integer.MAX_VALUE);
                } while (!weights.add(weight));

                Image[y*WIDTH + x] = new Pixel(true, false, weight, -1);
            }
        }

        System.out.println("Duplicate rns: " + dupCount + " of " + (WIDTH * HEIGHT) + " = " + (double)(dupCount) / ((double)(WIDTH*HEIGHT)));

        // Precalculate neighbors of every pixel
        System.out.println("Calculating neighbors...");
        assert (NEIGH_X.length == NEIGH_Y.length);
        ArrayList<Pixel> ng = new ArrayList<Pixel>();

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                ng.clear();
                for (int i = 0; i < NEIGH_X.length; i++) {
                    int y2 = y + NEIGH_Y[i];
                    if (y2 < 0 || y2 == HEIGHT) {
                        continue;
                    }
                    int x2 = x + NEIGH_X[i];
                    if (x2 < 0 || x2 == WIDTH) {
                        continue;
                    }
                    ng.add(Image[y2*WIDTH + x2]);
                }
                Image[y*WIDTH + x].neighbors = ng.toArray(new Pixel[ng.size()]);
            }
        }

        System.out.println("Initializing algorithm...");
        ALGORITHM.init(WIDTH, HEIGHT);

        System.out.println("Assigning colors...");
        for (int i = 0; i < colors.length; i++) {
            if (i % (32*4096) == 0) {
                System.out.println(((double) i * 100.0) / (WIDTH * HEIGHT) + "%...");
            }

            try {
                ALGORITHM.place(colors[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            if ((float) i / colors.length >= .95) { break;}
        }

        ALGORITHM.done();

        System.out.println("Verifying colors used...");
        boolean[] used = new boolean[MAX_COLORS*MAX_COLORS*MAX_COLORS];
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Color pix = Image[y*WIDTH + x].color;
                int r = pix.getRed();
                int g = pix.getGreen();
                int b = pix.getBlue();
                try {
                    if (used[r * 256 * 256 + g * 256 + b]) {
                        System.out.println("Color " + r + "/" + g + "/" +
                                b + " is added more than once!");
                    } else {
                        used[r * 256 * 256 + g * 256 + b] = true;
                    }
                } catch (Exception e) {
                    System.out.println(r + "/" + g + "/" + b);
                    System.out.println(r * 256 * 256 + g * 256 + b + " / " + used.length);
                    throw e;
                }
            }
        }

        BufferedImage img = new BufferedImage(WIDTH, HEIGHT,
                                    BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < WIDTH; x++){
            for (int y = 0; y < HEIGHT; y++){
                Color rgb = Image[y * WIDTH + x].color;
//                if (rgb == null) { rgb = Color.gray;}
                img.setRGB(x, y, rgb.getRGB());
            }
        }
        try {
            File outputFile = new File("/home/sky/Desktop/imgrgb_" +
                                       ALGORITHM.getName() + "_" +
                                       SORTER.getName() + "_" +
                                       START_X + "-" + START_Y +
                                       "s" + SEED + "-negwght.png");
            ImageIO.write(img, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
