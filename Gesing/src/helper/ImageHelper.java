/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
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

    public static void transformasiSpasial(GImage gimage, int type) {
        GImage copy = new GImage(deepCopy(gimage.getBufImage()));
        for (int i = 1; i < gimage.getBufImage().getHeight() - 1; i++) {
            for (int j = 1; j < gimage.getBufImage().getWidth() - 1; j++) {
                gimage.setRGB(j, i, calcAdjArray(getPixel3x3Array(copy, j, i), type));
            }
        }
    }

    public static void transformasiSpasial(GImage gimage, float[] weight) {
        GImage copy = new GImage(deepCopy(gimage.getBufImage()));
        for (int i = 1; i < gimage.getBufImage().getHeight() - 1; i++) {
            for (int j = 1; j < gimage.getBufImage().getWidth() - 1; j++) {
                gimage.setRGB(j, i, calcAdjArray(getPixel3x3Array(copy, j, i), weight));
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

    private static Color calcAdjArray(Color[][] pixelArray, float[] weight) {
        int tColR = 0;
        int tColG = 0;
        int tColB = 0;
        int sumPlusR = 0;
        int sumPlusG = 0;
        int sumPlusB = 0;
        int iWeight = 0;
        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 3; l++) {
                tColR = (int) (pixelArray[k][l].getRed() * weight[iWeight]);
                tColG = (int) (pixelArray[k][l].getGreen() * weight[iWeight]);
                tColB = (int) (pixelArray[k][l].getBlue() * weight[iWeight]);
                iWeight++;
                sumPlusR += tColR;
                sumPlusG += tColG;
                sumPlusB += tColB;
            }
        }
        if (sumPlusR > 255) {
            sumPlusR = 255;
        } else if (sumPlusR < 0) {
            sumPlusR = 0;
        }
        if (sumPlusG > 255) {
            sumPlusG = 255;
        } else if (sumPlusG < 0) {
            sumPlusG = 0;
        }
        if (sumPlusB > 255) {
            sumPlusB = 255;
        } else if (sumPlusB < 0) {
            sumPlusB = 0;
        }
        return new Color(sumPlusR, sumPlusG, sumPlusB);
    }

    /**
     *
     * @param pixelArray (3x3 array)
     * @param processType (1:min | 2:max | 3:ave)
     * @return new Pixel Value
     */
    private static Color calcAdjArray(Color[][] pixelArray, int processType) {
        switch (processType) {
            case 1: { // minimum
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
                        if (maxR > currR) {
                            maxR = currR;
                        }
                        if (maxG > currG) {
                            maxG = currG;
                        }
                        if (maxB > currB) {
                            maxB = currB;
                        }
                    }
                }
                max = new Color(maxR, maxG, maxB);
                return max;
            }
            case 2: { //maximum
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
            case 3: { //rata2
                int sumR = 0;
                int sumG = 0;
                int sumB = 0;
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        Color pixel = pixelArray[k][l];
                        sumR += pixel.getRed();
                        sumG += pixel.getGreen();
                        sumB += pixel.getBlue();
                    }
                }
                return new Color((sumR / 9), (sumG / 9), (sumB / 9));
            }
            case 4: { //homogen
                Color max = pixelArray[0][0];
                Color temp = pixelArray[1][1];
                int maxR = Math.abs(max.getRed() - temp.getRed());
                int maxG = Math.abs(max.getGreen() - temp.getGreen());
                int maxB = Math.abs(max.getBlue() - temp.getBlue());
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        if (!(k == 1 && l == 1)) {
                            Color pixel = pixelArray[k][l];
                            int currR = Math.abs(pixel.getRed() - temp.getRed());
                            int currG = Math.abs(pixel.getGreen() - temp.getGreen());
                            int currB = Math.abs(pixel.getBlue() - temp.getBlue());
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
                }
                max = new Color(maxR, maxG, maxB);
                return max;
            }
            case 5: { //difference
                Color max = pixelArray[0][1];
                Color temp = pixelArray[2][1];
                int maxR = Math.abs(max.getRed() - temp.getRed());
                int maxG = Math.abs(max.getGreen() - temp.getGreen());
                int maxB = Math.abs(max.getBlue() - temp.getBlue());
                for (int k = 0; k < 3; k++) {
                    max = pixelArray[k][0];
                    temp = pixelArray[2 - k][2];
                    int currR = Math.abs(max.getRed() - temp.getRed());
                    int currG = Math.abs(max.getGreen() - temp.getGreen());
                    int currB = Math.abs(max.getBlue() - temp.getBlue());
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
                max = new Color(maxR, maxG, maxB);
                return max;
            }
        }
        return pixelArray[1][1];

    }

    public static int[][] buildLUT(GImage gi) {
        int[][] lut = new int[3][256];
        int[][] harray = gi.getHistogram();
        for (int i = 0; i < lut.length; i++) {
            int minFreqSum = harray[i][0];
            int maxFreqSum = getMaxFreqSum(harray[i]);
            int currentFreqSum = 0;
            for (int j = 0; j < lut[i].length; j++) {
                currentFreqSum += harray[i][j];
                lut[i][j] = (currentFreqSum - minFreqSum) * 255 / (maxFreqSum - minFreqSum);
            }
        }
        return lut;
    }

    private static int getMaxFreqSum(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            max += arr[i];
        }
        return max;
    }

    private static void applyLUT(GImage gi, int[][] lut) {
        BufferedImage buff = gi.getBufImage();
        for (int i = 0; i < buff.getHeight(); i++) {
            for (int j = 0; j < buff.getWidth(); j++) {
                int currentColor = buff.getRGB(j, i);
                Color tCol = new Color(currentColor);
                int oldRed = tCol.getRed();
                int oldGreen = tCol.getGreen();
                int oldBlue = tCol.getBlue();

                Color col = new Color(lut[GImage.RED][oldRed], lut[GImage.GREEN][oldGreen], lut[GImage.BLUE][oldBlue]);
                gi.setRGB(j, i, col);
            }
        }
    }

    private static void flood(GImage img, boolean[][] mark, int x, int y, Color srcColor, Color tgtColor) {
        Queue<Point> queue = new LinkedList<Point>();
        queue.add(new Point(x, y));

        while (!queue.isEmpty()) {
            Point p = queue.remove();

            if ((p.x >= 0)
                    && (p.x < img.getBufImage().getWidth()
                    && (p.y >= 0)
                    && (p.y < img.getBufImage().getHeight()))) {
                Color currColor = img.getRGB(p.x, p.y);
                //System.out.println("color : " + currColor.getRed());
                if (!mark[p.y][p.x]
                        && isSameColor(currColor, srcColor)) {
                    mark[p.y][p.x] = true;
                    img.setRGB(p.x, p.y, tgtColor);
                    queue.add(new Point(p.x + 1, p.y));
                    queue.add(new Point(p.x - 1, p.y));
                    queue.add(new Point(p.x, p.y + 1));
                    queue.add(new Point(p.x, p.y - 1));
                }
            }
        }
    }

    private static boolean isSameColor(Color colorA, Color colorB) {
        return colorA.getRed() == colorB.getRed();
    }

    public static ArrayList< ArrayList<String>> recognizeObjects(GImage gi) {
        ArrayList< ArrayList<String>> stringObjList = new ArrayList<>();
        BufferedImage buff = gi.getBufImage();
        int width = buff.getWidth();
        int height = buff.getHeight();
        boolean[][] mark = new boolean[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color col = gi.getRGB(j, i);
                int red = col.getRed();
                //mark[j][i] = true;
                if (!isWhite(gi, j, i)) {
                    //ArrayList<String> stringObj = roundObject(gi, j, i);
                    //System.out.println(buildChainCode(gi, j - 1, i));
                    //stringObjList.add(stringObj);
                    flood(gi, mark, j, i, col, Color.WHITE);
                }
            }
        }

        return stringObjList;
    }

    public static ArrayList<String> getChainCodes(GImage img) {
        ArrayList<String> chainCodes = new ArrayList<>();
        BufferedImage buff = img.getBufImage();
        int width = buff.getWidth();
        int height = buff.getHeight();
        boolean[][] mark = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color col = img.getRGB(j, i);
                int red = col.getRed();
                //System.out.println("red = " + j + " " + i + " "+ red);
                //mark[j][i] = true;
                if (!isWhite(img, j, i)) {
                    //System.out.println("1st pass");
                    chainCodes.add(ChainCode.build(img, j, i));
                    //System.out.println("2nd pass");
                    flood(img, mark, j, i, col, Color.WHITE);
                    //System.out.println("3rd pass");
                }
            }
        }
        return chainCodes;
    }

    private static ArrayList<String> roundObject(GImage gi, int startX, int startY) {
        System.out.println(startX + " " + startY);
        ArrayList<String> stringObj = new ArrayList<>();
        int currentX = startX;
        int currentY = startY;
        int prevX = currentX;
        int prevY = currentY;
        do {
            currentY--;
            if (!isWhite(gi, currentX, currentY) && isWhite(gi, currentX - 1, currentY) && (currentX != prevX || currentY != prevY)) { //arah 1                  
                stringObj.add("1");
                prevY = currentY + 1;
            } else {
                currentX++;
                if (!isWhite(gi, currentX, currentY) && isWhite(gi, currentX - 1, currentY) && (currentX != prevX || currentY != prevY)) { //arah 2
                    stringObj.add("2");
                    prevX = currentX - 1;
                    prevY = currentY + 1;
                } else {
                    currentY++;
                    if (!isWhite(gi, currentX, currentY) && isWhite(gi, currentX, currentY - 1) && (currentX != prevX || currentY != prevY)) { //arah 3
                        stringObj.add("3");
                        prevX = currentX - 1;
                    } else {
                        currentY++;
                        if (!isWhite(gi, currentX, currentY) && isWhite(gi, currentX, currentY - 1) && (currentX != prevX || currentY != prevY)) { //arah 4
                            stringObj.add("4");
                            prevX = currentX - 1;
                            prevY = currentY - 1;
                        } else {
                            currentX--;
                            if (!isWhite(gi, currentX, currentY) && isWhite(gi, currentX + 1, currentY) && (currentX != prevX || currentY != prevY)) { //arah 5
                                stringObj.add("5");
                                prevY = currentY - 1;
                            } else {
                                currentX--;
                                if (!isWhite(gi, currentX, currentY) && isWhite(gi, currentX + 1, currentY) && (currentX != prevX || currentY != prevY)) { //arah 6
                                    stringObj.add("6");
                                    prevX = currentX + 1;
                                    prevY = currentY - 1;
                                } else {
                                    currentY++;
                                    if (!isWhite(gi, currentX, currentY) && isWhite(gi, currentX, currentY + 1) && (currentX != prevX || currentY != prevY)) { //arah 7
                                        stringObj.add("7");
                                        prevX = currentX + 1;
                                    } else {
                                        currentY++;
                                        if (!isWhite(gi, currentX, currentY) && isWhite(gi, currentX, currentY + 1) && (currentX != prevX || currentY != prevY)) { //arah 8
                                            stringObj.add("8");
                                            prevX = currentX + 1;
                                            prevY = currentY + 1;
                                        } else {// balik lagi
                                            int tmpPrevX = prevX;
                                            int tmpPrevY = prevY;
                                            prevX = currentX;
                                            prevY = currentY;
                                            currentX = tmpPrevX;
                                            currentY = tmpPrevY;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } while (currentX != startX || currentY != startY);
        return stringObj;
    }

    public static int countObject(GImage img) {
        int width = img.getBufImage().getWidth();
        int height = img.getBufImage().getHeight();
        int count = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int curPixel = img.getRGB(j, i).getRed();
                if (curPixel == 0) {
                    boolean[][] mark = new boolean[height][width];
                    flood(img, mark, j, i, Color.BLACK, Color.RED);
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    private static Boolean isWhite(GImage gi, int x, int y) {
        int color = gi.getRGB(x, y).getRed();
        return (color > 200);
    }

    public static void transformasiEkualisasi(GImage gi) {
        int[][] lut = buildLUT(gi);
        applyLUT(gi, lut);
    }

    public static void transformasiSpesifikasi(GImage gi, int[][] lut) {
        applyLUT(gi, lut);
    }

    public static void combineGradien(GImage img1, GImage img2) {
        for (int i = 0; i < img1.getHeight(); i++) {
            for (int j = 0; j < img1.getWidth(); j++) {
                Color col1 = img1.getRGB(j, i);
                Color col2 = img2.getRGB(j, i);
                img1.setRGB(j, i, combineColor(col1, col2));
            }
        }
    }

    private static Color combineColor(Color col1, Color col2) {
        int red1 = col1.getRed();
        int red2 = col2.getRed();
        int green1 = col1.getGreen();
        int green2 = col2.getGreen();
        int blue1 = col1.getBlue();
        int blue2 = col2.getBlue();
        return new Color(
                calcDistLikePhytagoras(red1, red2),
                calcDistLikePhytagoras(green1, green2),
                calcDistLikePhytagoras(blue1, blue2));
    }

    private static int calcDistLikePhytagoras(int num1, int num2) {
        //System.out.println("num1 = " + num1);
        //System.out.println("num2 = " + num2);
        int count = (int) Math.sqrt((num1 * num1) + (num2 * num2));
        //System.out.println("count = " + count);
        if (count > 255) {
            return 255;
        }
        if (count < 0) {
            return 0;
        }
        return count;
    }
}
