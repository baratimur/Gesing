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
  
  public static int[] buildLUT(GImage gi) {
    int[] lut = new int[256];
    
    return lut;
  }
  
  public GImage applyFunction(GImage gi, String func) {    
    GImage newGImage = new GImage(gi.getBufImage());
    int height = gi.getBufImage().getHeight();
    int width = gi.getBufImage().getWidth();
    
    //sumbu x pada histogram
    int aRed;  //base, rgb (0...255)
    int aGreen;  //base, rgb (0...255)
    int aBlue;  //base, rgb (0...255)
    int aAlpha;  //base, rgb (0...255)

    if(func=="power") {      
      int b;   //exponent
      for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {                
              aRed = gi.getRGB(i, j).getRed();
              aGreen = gi.getRGB(i, j).getGreen();
              aBlue = gi.getRGB(i, j).getBlue();
              aAlpha = gi.getRGB(i, j).getAlpha();
              b = 2;
              
              int red = (int) Math.pow(aRed, b);
              int green = (int) Math.pow(aGreen, b);
              int blue = (int) Math.pow(aBlue, b);
              int alpha = (int) Math.pow(aAlpha, b);             
              int newRGB = (new Color(red, green, blue, alpha)).getRGB();
              Color color = new Color(newRGB);
              newGImage.setRGB(i, j, color);
            }
        }
    } else if(func=="sin") {      
      for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {   
              aRed = gi.getRGB(i, j).getRed();
              aGreen = gi.getRGB(i, j).getGreen();
              aBlue = gi.getRGB(i, j).getBlue();
              aAlpha = gi.getRGB(i, j).getAlpha();
              
              int red = (int) Math.sin(aRed);
              int green = (int) Math.sin(aGreen);
              int blue = (int) Math.sin(aBlue);
              int alpha = Math.sin(aAlpha);             
              int newRGB = (new Color(red, green, blue, alpha)).getRGB();
              Color color = new Color(newRGB);
              newGImage.setRGB(i, j, color);
            }
        }
    }
    
    return newGImage;
  }    
  
  public static BufferedImage deepCopy(BufferedImage bi) {
      ColorModel cm = bi.getColorModel();
      boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
      WritableRaster raster = bi.copyData(null);
      return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
}
}
