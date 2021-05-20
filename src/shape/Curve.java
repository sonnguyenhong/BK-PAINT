/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shape;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

/**
 *
 * @author KA
 */
public class Curve extends Shape implements DrawType {
    private int state = 1;
    
    private ArrayList<Point> list; 
    private ArrayList<Point> listPointToState1;
    private ArrayList<Point> listPointToState2;
    private ArrayList<Point> listPointToState3;
    
    public Curve() {
        list = new ArrayList<>();
        listPointToState1 = new ArrayList<>();
        listPointToState2 = new ArrayList<>();
        listPointToState3 = new ArrayList<>();
        Point p;
        for(int i = 0; i < 4; i++){
            p = new Point(0, 0);
            list.add(p);
        }
        state = 1;
    }
    //trả state về 1
     public void resetState(){
        this.state = 1;
    }
    
     //tăng state lên 1
    public void incState(){
        this.state++;
    }
    
    public void setList(ArrayList<Point> list){
        this.list = list;
    }
    public void setStartPoint(Point start){
        this.start = start;
    }
   
    public void addPointToState(Point p){
        switch(state){
            case 1:
                listPointToState1.add(p);
                break;
            case 2:
                listPointToState2.add(p);
                break;
            case 3:
                listPointToState3.add(p);
                break;
        }
    }
    public int getSizeOfStateFirst(){
        return listPointToState1.size();
    }
    public int getSizeOfStateSecond(){
        return listPointToState1.size()+listPointToState2.size();
    }
    
    public int getState(){
        return state;
    }
    
    public void setState(int state){
        this.state = state;
    }
    
    @Override
    public void draw(Graphics2D g2d){
        BasicStroke stroke = new BasicStroke(strokeThickness,endStrokeCap,lineStrokeJoin,miterLimit, dashArray,dashPhase);
        g2d.setStroke(stroke);
        g2d.setColor(strokeColor);
        if(state == 1){ 
            list.get(2).setLocation(list.get(3));
            list.get(1).setLocation(list.get(3));   
        }
        else if(state == 2){
            list.get(1).setLocation(list.get(2)); 
        }
        GeneralPath gP = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        gP.moveTo(list.get(0).x, list.get(0).y);  
        gP.curveTo(list.get(1).x, list.get(1).y, list.get(2).x, list.get(2).y, list.get(3).x, list.get(3).y); 
        g2d.draw(gP);
    }
    
    //Lấy list ra
    public ArrayList<Point> getList(){
        return list;
    }
}

