/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import model.GImage;

/**
 *
 * @author Bara Timur
 */
public class ChainCode {

    private String codes;
    private int[] frequency;
    private int[] frequencyPercentage;
    private String shape;

    public ChainCode(String codes) {
        this.codes = codes;
        frequency = countSum(this.codes);
        frequencyPercentage = countPercentage(frequency);
        recognizeShape();
    }

    private int[] countPercentage(int[] freq) {
        int[] count = new int[8];
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            count[i] = 0;
            sum += freq[i];
        }
        //System.out.println("sum = " + sum);
        for (int i = 0; i < 8; i++) {
            count[i] = (freq[i] * 100) / sum;
        }
        return count;
    }

    private int[] countSum(String codes) {
        int[] count = new int[9];
        for (int i = 0; i < 8; i++) {
            count[i] = 0;
        }
        for (int i = 0; i < codes.length(); i++) {
            count[Integer.parseInt(codes.charAt(i) + "")]++;
        }
        return count;
    }

    private void recognizeShape() {
        int moreThanOne = 0;
        for (int i = 0; i < 8; i++) {
            if (frequencyPercentage[i] > 1) {
                moreThanOne++;
            }
        }
        if(moreThanOne < 3) {
            shape = "Line";
        } else if(moreThanOne == 4) {
            shape = "Rectangle";
        } else if(moreThanOne == 5) {
            shape = "Triangle";
        } else if (moreThanOne > 5) {
            shape = "Circle";
        }
    }

    public void print() {
        System.out.println("codes = " + codes);
        for (int i = 0; i < 8; i++) {
            System.out.println(i + " : " + this.frequency[i] + " -> " + this.frequencyPercentage[i]);
        }
        System.out.println("shape = " + shape);
    }

    public static String build(GImage img, int startX, int startY) {
        StringBuilder sb = new StringBuilder();
        Point cur = new Point(startX, startY);
        int[] nextDirs;
        Point ahead;
        int i;
        int curDir = 0;
        boolean isEnd = false;
        do {
            nextDirs = nextDirections(curDir);
            i = 0;
            //System.out.println("P0 : " + cur.x + " " + cur.y + " " + img.getRGB(cur.x, cur.y));
            boolean isValid = false;
            do {
                ahead = moveAgent(cur, nextDirs[i]);
                //System.out.println("Pa : " + ahead.x + " " + ahead.y + " " + img.getRGB(ahead.x, ahead.y));
                if (!isWater(img.getRGB(ahead.x, ahead.y))) {
                    Point[] leftPoint = getLeftSidePoint(ahead, nextDirs[i]);
                    //System.out.println("Pl : " + leftPoint.x + " " + leftPoint.y + " " + img.getRGB(leftPoint.x, leftPoint.y));
                    if (isValidIsland(img.getRGB(ahead.x, ahead.y), img.getRGB(leftPoint[0].x, leftPoint[0].y))) {
                        sb.append(nextDirs[i]);
                        isValid = true;
                        cur = ahead;
                        curDir = nextDirs[i];
                        //System.out.println("curDir = " + curDir);
                    } else {
                        if (leftPoint.length == 2) {
                            if (isValidIsland(img.getRGB(ahead.x, ahead.y), img.getRGB(leftPoint[1].x, leftPoint[1].y))) {
                                sb.append(nextDirs[i]);
                                isValid = true;
                                cur = ahead;
                                curDir = nextDirs[i];
                                //System.out.println("curDir = " + curDir);
                            } else {
                                i++;
                            }
                        } else {
                            i++;
                        }
                    }
                } else {
                    i++;
                }
                if (i == 8) {
                    isValid = true;
                }
            } while (!isValid);
//            System.out.print(cur.x + " " + cur.y);
//            System.out.print(startX + " " + startY);
//            System.out.print(cur.x == startX);
//            System.out.println(cur.y == startY);
            isEnd = (cur.x == startX && cur.y == startY);
        } while (!isEnd);
        return sb.toString();
    }

    private static boolean isValidIsland(Color colA, Color colB) {
        return colA.getRed() != colB.getRed();
    }

    private static Point[] getLeftSidePoint(Point p, int direction) {
        Point[] retP;
        if (direction == 0 || direction == 2 || direction == 4 || direction == 6) {
            retP = new Point[1];
            retP[0] = moveAgent(p, (direction + 2) % 8);
            return retP;
        } else {
            retP = new Point[2];
            retP[0] = moveAgent(p, (direction + 1) % 8);
            retP[1] = moveAgent(p, (direction + 3) % 8);
            return retP;
        }
    }

    private static boolean isWater(Color col) {
        return col.getRed() > 200;
    }

    private static int[] nextDirections(int curDir) {
        int[] dirs = new int[8];
        for (int i = 0; i < 8; i++) {
            dirs[i] = (curDir + 11 - i) % 8;
        }
        return dirs;
    }

    /*
     * direction
     * 3 2 1
     * 4 x 0
     * 5 6 7
     */
    private static Point moveAgent(Point point, int direction) {
        Point tP = new Point(point);
        switch (direction) {
            case 0:
                tP.setLocation(tP.getX() + 1, tP.getY());
                break;
            case 1:
                tP.setLocation(tP.getX() + 1, tP.getY() - 1);
                break;
            case 2:
                tP.setLocation(tP.getX(), tP.getY() - 1);
                break;
            case 3:
                tP.setLocation(tP.getX() - 1, tP.getY() - 1);
                break;
            case 4:
                tP.setLocation(tP.getX() - 1, tP.getY());
                break;
            case 5:
                tP.setLocation(tP.getX() - 1, tP.getY() + 1);
                break;
            case 6:
                tP.setLocation(tP.getX(), tP.getY() + 1);
                break;
            case 7:
                tP.setLocation(tP.getX() + 1, tP.getY() + 1);
                break;
        }
        return tP;
    }
}
