/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shape;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

/**
 *
 * @author KA
 */
public class Triangle extends Shape implements DrawType{
    private Color fillColor;
    
    @Override
    public void draw(Graphics2D g2d){
      BasicStroke stroke = new BasicStroke(strokeThickness, endStrokeCap, lineStrokeJoin, miterLimit, dashArray, dashPhase);
      g2d.setStroke(stroke);
      if(start.x < end.x && start.y < end.y){
          int[] x = {end.x, start.x+(end.x-start.x)/2, start.x};
          int[] y = {end.y, start.y, end.y};
          if (fillColor != Color.WHITE && !fillColor.equals(new Color(255, 255, 255))) {
            g2d.setColor(fillColor);
            g2d.fillPolygon(x, y, 3);
          }
          g2d.setColor(strokeColor);
          g2d.drawPolygon(x, y, 3);
      }
      else if(start.x < end.x && start.y > end.y){
          int[] x = {start.x+(end.x-start.x)/2, start.x, end.x};
          int[] y = {end.y, start.y, start.y};
          if (fillColor != Color.WHITE && !fillColor.equals(new Color(255, 255, 255))) {
            g2d.setColor(fillColor);
            g2d.fillPolygon(x, y, 3);
          }
          g2d.setColor(strokeColor);
          g2d.drawPolygon(x, y, 3);
      }
      else if(start.x > end.x && start.y < end.y){
          int[] x = {start.x - (start.x - end.x)/2, end.x, start.x};
          int[] y = {start.y, end.y, end.y};
          if (fillColor != Color.WHITE && !fillColor.equals(new Color(255, 255, 255))) {
            g2d.setColor(fillColor);
            g2d.fillPolygon(x, y, 3);
          }
          g2d.setColor(strokeColor);
          g2d.drawPolygon(x, y, 3);
      }
      else if(start.x > end.x && start.y > end.y){
          int[] x = {start.x - (start.x - end.x)/2, end.x, start.x};
          int[] y = {end.y, start.y, start.y};
          if (fillColor != Color.WHITE && !fillColor.equals(new Color(255, 255, 255))) {
            g2d.setColor(fillColor);
            g2d.fillPolygon(x, y, 3);
          }
          g2d.setColor(strokeColor);
          g2d.drawPolygon(x, y, 3);
      }
    }
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }
}
