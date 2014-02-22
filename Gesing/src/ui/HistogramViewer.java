/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

/**
 *
 * @author Bara Timur
 */
//public class HistogramViewer extends JInternalFrame {
public class HistogramViewer extends JPanel {

    protected static final int MIN_BAR_WIDTH = 4;
    private int[] histogramData;
    private Color color;

    public HistogramViewer(int[] histogramData, Color color) {
        this.histogramData = histogramData;
        this.color = color;
        int width = (256 * MIN_BAR_WIDTH) + 11;
        Dimension minSize = new Dimension(width, 128);
        Dimension prefSize = new Dimension(width, 256);
        setMinimumSize(minSize);
        setPreferredSize(prefSize);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (histogramData != null) {
            int xOffset = 5;
            int yOffset = 5;
            int width = getWidth() - 1 - (xOffset * 2);
            int height = getHeight() - 1 - (yOffset * 2);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawRect(xOffset, yOffset, width, height);
            int barWidth = Math.max(MIN_BAR_WIDTH,
                    (int) Math.floor((float) width
                    / (float) 256));
            //System.out.println("width = " + width + "; size = "
            //        + 256 + "; barWidth = " + barWidth);
            int maxValue = 0;

            for (int j = 0; j < 256; j++) {
                if (histogramData[j] > maxValue) {
                    maxValue = histogramData[j];
                }
            }


            int xPos = xOffset;
            for (int j = 0; j < 256; j++) {
                int value = histogramData[j];
                int barHeight = Math.round(((float) value
                        / (float) maxValue) * height);

                int yPos = height + yOffset - barHeight;
                g2d.setColor(color);
                Rectangle2D bar = new Rectangle2D.Float(
                        xPos, yPos, barWidth, barHeight);
                g2d.fill(bar);
                g2d.setColor(Color.DARK_GRAY);
                g2d.draw(bar);
                xPos += barWidth;
            }
            g2d.dispose();
        }
    }
}