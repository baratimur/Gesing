/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
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
    
    public static void transformasiSpasial(GImage gimage, int[] weight) {
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

    private static Color calcAdjArray(Color[][] pixelArray, int[] weight) {
        int tColR = 0;
        int tColG = 0;
        int tColB = 0;
        int sumPlusR = 0;
        int sumPlusG = 0;
        int sumPlusB = 0;
        int iWeight = 0;
        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 3; l++) {
                tColR = pixelArray[k][l].getRed() * weight[iWeight];
                tColG = pixelArray[k][l].getGreen() * weight[iWeight];
                tColB = pixelArray[k][l].getBlue() * weight[iWeight];
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
            case 1: {
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
            case 3: {
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

    private static void flood(GImage img, boolean[][] mark,
            int row, int col, Color srcColor, Color tgtColor) {
        // make sure row and col are inside the image
        if (row < 0) {
            return;
        }
        if (col < 0) {
            return;
        }
        if (row >= img.getBufImage().getHeight()) {
            return;
        }
        if (col >= img.getBufImage().getWidth()) {
            return;
        }

        // make sure this pixel hasn't been visited yet
        if (mark[row][col]) {
            return;
        }

        // make sure this pixel is the right color to fill
        if (!img.getRGB(col, row).equals(srcColor)) {
            return;
        }

        // fill pixel with target color and mark it as visited
        img.setRGB(col, row, tgtColor);
        mark[row][col] = true;

        // recursively fill surrounding pixels
        // (this is equivelant to depth-first search)
        flood(img, mark, row - 1, col, srcColor, tgtColor);
        flood(img, mark, row + 1, col, srcColor, tgtColor);
        flood(img, mark, row, col - 1, srcColor, tgtColor);
        flood(img, mark, row, col + 1, srcColor, tgtColor);
    }
    
    public static ArrayList< ArrayList<String> > recognizeObject(GImage gi) {
      ArrayList< ArrayList<String> > stringObjList = new ArrayList<>();
      BufferedImage buff = gi.getBufImage();      
      int width = buff.getWidth();
      int height = buff.getHeight();      
      for (int i=0; i<height; i++) {
        for (int j=0; j<width; j++) {
          Color col = gi.getRGB(j, i);
          int red = col.getRed();
          if (!isWhite(gi, j, i)) {
            roundObject(gi, j, i);
          }
        }
      }
      
      return stringObjList;
    }
    
    private static ArrayList<String> roundObject(GImage gi, int startX, int startY) {
      ArrayList<String> stringObj = new ArrayList<>();      
      int currentX = startX;
      int currentY = startY;      
      int prevX = currentX;
      int prevY = currentY;
      do {        
        currentY--;
        if (!isWhite(gi, currentX, currentY) && isWhite(gi, currentX-1, currentY) && (currentX != prevX || currentY != prevY)) { //arah 1                  
          stringObj.add("1"); 
          prevY = currentY + 1;
        } else {
          currentX++;
          if (!isWhite(gi, currentX, currentY) && isWhite(gi, currentX-1, currentY) && (currentX != prevX || currentY != prevY)) { //arah 2
            stringObj.add("2");
            prevX = currentX - 1;
            prevY = currentY + 1;
          } else {            
            currentY++;
            if (!isWhite(gi, currentX, currentY) && isWhite(gi, currentX, currentY-1) && (currentX != prevX || currentY != prevY)) { //arah 3
              stringObj.add("3");
              prevX = currentX - 1;              
            } else {
              currentY++;
              if (!isWhite(gi, currentX, currentY) && isWhite(gi, currentX, currentY-1) && (currentX != prevX || currentY != prevY)) { //arah 4
                stringObj.add("4");
                prevX = currentX - 1;
                prevY = currentY - 1;
              } else {
                currentX--;
                if (!isWhite(gi, currentX, currentY) && isWhite(gi, currentX+1, currentY) && (currentX != prevX || currentY != prevY)) { //arah 5
                  stringObj.add("5");
                  prevY = currentY - 1;
                } else {
                  currentX--;
                  if (!isWhite(gi, currentX, currentY) && isWhite(gi, currentX+1, currentY) && (currentX != prevX || currentY != prevY)) { //arah 6
                    stringObj.add("6");
                    prevX = currentX + 1;
                    prevY = currentY - 1;
                  } else {
                    currentY++;
                    if (!isWhite(gi, currentX, currentY) && isWhite(gi, currentX, currentY+1) && (currentX != prevX || currentY != prevY)) { //arah 7
                      stringObj.add("7");
                      prevX = currentX + 1;
                    } else {
                      currentY++;
                      if (!isWhite(gi, currentX, currentY) && isWhite(gi, currentX, currentY+1) && (currentX != prevX || currentY != prevY)) { //arah 8
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
}
