/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author HP
 */
public class ImageCell extends ImageIcon {
    private final int WIDTH = 100;
    private final int HEIGHT = 125;
    private Image img = null;
    
    public ImageCell(ImageIcon imageIcon){
        this.img = imageIcon.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
    }
    
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y){
        if(img != null){
            g.drawImage(img, x+1, y+1, img.getWidth(null)-2, img.getHeight(null)-2, null);
        }
    }
}
