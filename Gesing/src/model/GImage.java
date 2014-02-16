/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 *
 * @author Bara Timur
 */
public class GImage extends BufferedImage {

    public GImage(int i, int i1, int i2) {
        super(i, i1, i2);
    }

    @Override
    public int[] getRGB(int i, int i1, int i2, int i3, int[] ints, int i4, int i5) {
        return super.getRGB(i, i1, i2, i3, ints, i4, i5); //To change body of generated methods, choose Tools | Templates.
    }

    
}
