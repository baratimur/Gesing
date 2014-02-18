/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 *
 * @author Bara Timur
 */
public class GImage {
    
    public static final int RED = 0;
    public static final int GREEN = 1;
    public static final int BLUE = 2;
    
    
    private BufferedImage bufImage;
    private int height;
    private int width;
    private int RGBArray[][];
    private int HArray[][];
    private Color RGBColorArray[][];

    public GImage(BufferedImage bufImage) {
        this.bufImage = bufImage;
        init();
        fillArray();
    }

    private void init() {
        height = bufImage.getHeight();
        width = bufImage.getWidth();

        RGBArray = new int[height][width];
        RGBColorArray = new Color[height][width];
        HArray = new int[3][255];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 255; j++) {
                HArray[i][j] = 0;
            }
        }
    }

    private void fillArray() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int color = bufImage.getRGB(i, j);
                RGBArray[i][j] = color;
                Color tCol = new Color(color);
                RGBColorArray[i][j] = tCol;
                HArray[RED][tCol.getRed()]++;
                HArray[GREEN][tCol.getGreen()]++;
                HArray[BLUE][tCol.getBlue()]++;
            }
        }
    }

    public BufferedImage getBufImage() {
        return bufImage;
    }

    public Color[][] getRGBColorArray() {
        return RGBColorArray;
    }

    public Color[] getRGBColorLine(int row) {
        return RGBColorArray[row];
    }

    public Color getRGB(int x, int y) {
        return RGBColorArray[y][x];
    }

    public void setRGB(int x, int y, Color color) {
        RGBArray[y][x] = color.getRGB();
        RGBColorArray[y][x] = color;
        bufImage.setRGB(x, y, color.getRGB());
    }

    public void setRGB(int row, Color[] rowColor) {
        RGBColorArray[row] = rowColor;
        for (int i = 0; i < width; i++) {
            Color color =  rowColor[i];
            RGBArray[row][i] = color.getRGB();
            bufImage.setRGB(row, i, color.getRGB());
        }
    }
    
    /**
     * 
     * @param color (see static var)
     * @return array Histogram
     */
    public int[] getHistogramArray(int color) {
        return HArray[color];
    }
    
    public int[][] getHistogram() {
        return HArray;
    }
}
