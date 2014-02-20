/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bara Timur
 */
public class FileHelper {

    public static String openDoc(String pPathFile) {
        StringBuilder tRetval = new StringBuilder();
        File tFile = new File(pPathFile);
        if (tFile.exists() && tFile.isFile()) {
            try {
                FileInputStream tFis = new FileInputStream(tFile);
                DataInputStream tDis = new DataInputStream(tFis);
                while (tDis.available() != 0) {
                    tRetval.append((char) tDis.read());
                }
                tDis.close();
                tFis.close();
            } catch (IOException ex) {
                Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Gagal");
        }
        return tRetval.toString();
    }

    public static void saveDoc(String pPathFile, String pContent) {
        File tFile = new File(pPathFile);
        try {
            FileOutputStream tFos = new FileOutputStream(tFile);
            DataOutputStream tDos = new DataOutputStream(tFos);
            tDos.write(pContent.getBytes());
            tDos.close();
            tFos.close();
        } catch (IOException ex) {
            Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static int[] parseToLUT(String data) {
        String[] splitString = data.split(";");
        int[] LUT = new int[256];
        for (int i = 0; i < 256; i++) {
            LUT[i] = Integer.parseInt(splitString[i]);
        }
        return LUT;
    }

    public static String parseToData(int[] LUT) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 255; i++) {
            sb.append(LUT[i]);
            sb.append(";");
        }
        sb.append(LUT[255]);
        return sb.toString();
    }
}
