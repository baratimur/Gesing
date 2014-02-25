/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.awt.Color;
import java.awt.Point;
import model.GImage;

/**
 *
 * @author Bara Timur
 */
public class ChainCode {

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
                    Point leftPoint = getLeftSidePoint(ahead, nextDirs[i]);
                    //System.out.println("Pl : " + leftPoint.x + " " + leftPoint.y + " " + img.getRGB(leftPoint.x, leftPoint.y));
                    if (isValidIsland(img.getRGB(ahead.x, ahead.y), img.getRGB(leftPoint.x, leftPoint.y))) {
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

    private static Point getLeftSidePoint(Point p, int direction) {
        if (direction == 0 || direction == 2 || direction == 4 || direction == 6) {
            return moveAgent(p, (direction + 2) % 8);
        } else {
            return moveAgent(p, (direction + 1) % 8);
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
