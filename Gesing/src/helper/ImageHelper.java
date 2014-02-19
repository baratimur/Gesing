/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import model.GImage;

/**
 *
 * @author Bara Timur
 */
public class ImageHelper {

    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static void transformasiSpasial(GImage gimage) {
        GImage copy = new GImage(deepCopy(gimage.getBufImage()));
        for (int i = 1; i < gimage.getBufImage().getHeight()-1; i++) {
            for (int j = 1; j < gimage.getBufImage().getWidth()-1; j++) {
                gimage.setRGB(i, j, calcAdjArray(getPixel3x3Array(copy, i, j), 2));
            }
        }

    }

    private static Color[][] getPixel3x3Array(GImage gimage, int xPos, int yPos) {
        Color[][] pixelArray = new Color[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                pixelArray[i][j] = gimage.getRGB((xPos + i - 1), (yPos + j - 1));
            }
        }
        return pixelArray;
    }

    /**
     *
     * @param pixelArray (3x3 array)
     * @param processType (1:highFilter | 2:max)
     * @return new Pixel Value
     */
    private static Color calcAdjArray(Color[][] pixelArray, int processType) {
        switch (processType) {
            case 1: {
                int tCol = 0;
                int sumPlus = 0;
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        if (k == 1 && l == 1) {
                            //tCol = pixelArray[k][l] * 9;
                        } else {
                            //tCol = pixelArray[k][l] * -1;
                        }

                        sumPlus += tCol;
                    }
                }
                if (sumPlus > 255) {
                    sumPlus = 255;
                }
                if (sumPlus < 0) {
                    sumPlus = 0;
                }
                //return sumPlus;
                return null;
            }
            case 2: {
                Color max = pixelArray[0][0];
                int maxR = max.getRed();
                int maxG = max.getGreen();
                int maxB = max.getBlue();
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        Color pixel = pixelArray[k][l];
                        int currR = pixel.getRed();
                        int currG = pixel.getGreen();
                        int currB = pixel.getBlue();
                        if (maxR < currR) {
                            maxR = currR;
                        }
                        if (maxG < currG) {
                            maxG = currG;
                        }
                        if (maxB < currB) {
                            maxB = currB;
                        }
                    }
                }
                max = new Color(maxR, maxG, maxB);
                return max;
            }
        }
        return pixelArray[1][1];

    }
}
