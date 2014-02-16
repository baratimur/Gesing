/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Bara Timur
 */
public class PictureViewer extends JInternalFrame {

    private BufferedImage image;

    public PictureViewer(String title, BufferedImage image) throws HeadlessException {
        this.image = image;
        this.setTitle(title);
        try {
            javax.swing.UIManager.setLookAndFeel("Windows");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PictureViewer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(PictureViewer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(PictureViewer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(PictureViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setClosable(true);
    }

    private JLabel getImage() {
        if (image == null) {
            return null;
        }         // no image available
        ImageIcon icon = new ImageIcon(image);
        return new JLabel(icon);
    }

    public void showForm() {

        this.setContentPane(getImage());
        // f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);

        // draw
        this.repaint();
    }
}
