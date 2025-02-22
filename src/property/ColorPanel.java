/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package property;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.ImageIcon;


public class ColorPanel extends javax.swing.JPanel {
    private int soDong = 3;
    private int soCot = 10;
    private ColorCell[][] cells = new ColorCell[soDong][soCot];
    private Color[][] colors = new Color[soDong][soCot];
    private ColorCell image;
    private Graphics2D g2d;
    private Point location;

    /**
     * Creates new form ColorPanel
     */
    public ColorPanel() {
        initComponents();
        image = new ColorCell(Color.WHITE);
        
        colors[0][0] = new Color(0, 0, 0);
        colors[0][1] = new Color(255, 0, 204);
        colors[0][2] = new Color(204, 204, 255);
        colors[0][3] = new Color(255, 0, 0);
        colors[0][4] = new Color(204, 102, 0);
        colors[0][5] = new Color(255, 255, 0);
        colors[0][6] = new Color(0, 153, 0);
        colors[0][7] = new Color(0, 153, 255);
        colors[0][8] = new Color(0, 0, 255);
        colors[0][9] = new Color(153, 0, 153);
        colors[1][0] = Color.WHITE;
        colors[1][1] = new Color(255, 153, 102);
        colors[1][2] = new Color(255, 174, 201);
        colors[1][3] = new Color(255, 201, 14);
        colors[1][4] = new Color(229, 238, 176);
        colors[1][5] = new Color(155, 153, 200);
        colors[1][6] = new Color(214, 102, 202);
        colors[1][7] = new Color(153, 51, 0);
        colors[1][8] = new Color(112, 146, 190);
        colors[1][9] = new Color(200, 191, 231);
        colors[2][0] = new Color(50, 150, 150);
        colors[2][1] = new Color(100, 100, 100);
        colors[2][2] = new Color(150, 150, 150);
        colors[2][3] = new Color(155, 253, 0);
        colors[2][4] = new Color(150, 100, 75);
        colors[2][5] = new Color(100, 155, 75);
        colors[2][6] = new Color(230, 120, 103);
        colors[2][7] = new Color(153, 151, 0);
        colors[2][8] = new Color(134, 255, 190);
        colors[2][9] = new Color(20, 75, 100);
        this.setPreferredSize(new Dimension(250, 75));
        // tạo các ô màu
        for (int i = 0; i < soDong; i++) {
            for (int j = 0; j < soCot; j++) {
                cells[i][j] = new ColorCell(colors[i][j]);
            }
        }
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                setImage(e.getX(), e.getY());
            }

            public void mouseExited(MouseEvent e) {
                location = null;
                repaint();
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                location = e.getPoint();
                repaint();
            }
        });
        
        
    }
    
    public Color getColor(){
        return image.getColor();
    }
    
    public void setImage(int mouseX, int mouseY) {
        // nếu trỏ vào đường biên thì trả về
        if (mouseX % ColorCell.HEIGHT == 0 || mouseY % ColorCell.WIDTH == 0) {
            return;
        }
        int dong = mouseY / ColorCell.HEIGHT;
        int cot = mouseX / ColorCell.WIDTH;
        // nếu trỏ vào bên ngoài các ô thì trả về
        if (cot < 0 || cot > soCot - 1 || dong < 0 || dong > soDong - 1) {
            return;
        }
        image = cells[dong][cot];
    }
    
    public void setImage(Color color) {
        image = new ColorCell(color);
    }
    
    public void drawCell(int mouseX, int mouseY) {
        if (mouseX % ColorCell.HEIGHT == 0 || mouseY % ColorCell.WIDTH == 0) {
            return;
        }
        int dong = mouseY / ColorCell.HEIGHT;
        int cot = mouseX / ColorCell.WIDTH;
        if (cot < 0 || cot > soCot - 1 || dong < 0 || dong > soDong - 1) {
            return;
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        g2d.setPaint(Color.BLUE);
        g2d.fillRect(cot * ColorCell.WIDTH, dong * ColorCell.HEIGHT, ColorCell.WIDTH, ColorCell.HEIGHT);

    }
    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g;
        if (image == null) {
            this.createImage(ColorCell.WIDTH, ColorCell.HEIGHT);
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        for (int i = 0; i < soDong; i++) {
            for (int j = 0; j < soCot; j++) {
                g.setColor(colors[i][j]);
                g.fillRect(j * ColorCell.WIDTH + 1, i * ColorCell.HEIGHT - 1, ColorCell.WIDTH - 2, ColorCell.HEIGHT - 2);
            }
        }
        if (location != null) {
            drawCell(location.x, location.y);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(null);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
