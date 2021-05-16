/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author KA
 */
public class Bucket extends Shape implements DrawType{
    private Point start;
    private Color color;
    private boolean filled = true;
    
    private ArrayList<Point> arrPoint = new ArrayList<Point>();
    
    public void setStart(Point start){
        this.start = start;
    }
    
    @Override
    public Point getStart(){
        return this.start;
    }
    public void setColor(Color color){
        this.color = color;
    }
    public Color getColor(){
        return this.color;
    }
    public void draw(BufferedImage img){
        boundaryFill(img);
    }
    
    public boolean checkFilled() {
        return filled;
    }
    
    private void boundaryFill(BufferedImage image) {
        // tra ve mau tai diem bat dau
        int startColor = image.getRGB(this.start.x, this.start.y);
        // mau duoc chon
        int fillColor = this.color.getRGB();
        // Neu mau duoc chon trung vs mau tai diem bat dau thi ket thuc
        if (startColor == fillColor) {
            return;
        }
        // Doi mau tai diem bat dau thanh mau duoc chon
        image.setRGB(this.start.x, this.start.y, fillColor);


        ArrayList<Point> listPoint = new ArrayList<>();
        listPoint.add(this.start);
        while (!listPoint.isEmpty()) {
            Point temp = listPoint.get(0);
            if ((temp.x >= 0 && temp.x <= image.getWidth() - 2 && temp.y >= 0) && temp.y <= image.getHeight() - 2) {
                
                if (temp.x - 1 >= 0 && temp.y - 1 >= 0 && (image.getRGB(temp.x - 1, temp.y - 1) == startColor)) {
                    image.setRGB(temp.x - 1, temp.y - 1, fillColor);
                    listPoint.add(new Point(temp.x - 1, temp.y - 1));
                }
                if (temp.x - 1 >= 0 && image.getRGB(temp.x - 1, temp.y) == startColor) {
                    image.setRGB(temp.x - 1, temp.y, fillColor);
                    listPoint.add(new Point(temp.x - 1, temp.y));
                }
                if (temp.x - 1 >= 0 && image.getRGB(temp.x - 1, temp.y + 1) == startColor) {
                    image.setRGB(temp.x - 1, temp.y + 1, fillColor);
                    listPoint.add(new Point(temp.x - 1, temp.y + 1));
                }
                if (temp.y - 1 >= 0 && image.getRGB(temp.x, temp.y - 1) == startColor) {
                    image.setRGB(temp.x, temp.y - 1, fillColor);
                    listPoint.add(new Point(temp.x, temp.y - 1));
                }
                if (image.getRGB(temp.x, temp.y + 1) == startColor) {
                    image.setRGB(temp.x, temp.y + 1, fillColor);
                    listPoint.add(new Point(temp.x, temp.y + 1));
                }
                if (temp.x - 1 >= 0 && image.getRGB(temp.x - 1, temp.y + 1) == startColor) {
                    image.setRGB(temp.x - 1, temp.y + 1, fillColor);
                    listPoint.add(new Point(temp.x - 1, temp.y + 1));
                }
                if (image.getRGB(temp.x, temp.y + 1) == startColor) {
                    image.setRGB(temp.x, temp.y + 1, fillColor);
                    listPoint.add(new Point(temp.x, temp.y + 1));
                }
                if (image.getRGB(temp.x + 1, temp.y + 1) == startColor) {
                    image.setRGB(temp.x + 1, temp.y + 1, fillColor);
                    listPoint.add(new Point(temp.x + 1, temp.y + 1));
                }

            } else {

            }
            listPoint.remove(0);
        }

    }
     public Point getPoint() {
        return this.start;
    }

    public void setArrPoint(Point point) {
        arrPoint.add(point);
    }

    public ArrayList<Point> getArrPoint() {
        return arrPoint;
    }

    @Override
    public void draw(Graphics2D g2d) {
        
    }
}
