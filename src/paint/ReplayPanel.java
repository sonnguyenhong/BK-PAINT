/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import shape.Bucket;
import shape.Curve;
import shape.DrawType;
import shape.Eraser;
import shape.Line;
import shape.Oval;
import shape.Pencil;
import shape.Rectangle;
import shape.SelectionShape;
import shape.Triangle;

/**
 *
 * @author HP
 */
public class ReplayPanel extends javax.swing.JPanel implements Runnable{

    /**
     * Creates new form ReplayPanel
     */
    
    private BufferedImage buff_img, org_img;
    private Image img, last_img;
    private PaintState paintState;
    private Line line;
    private Rectangle rect;
    private Oval oval;
    
    private Triangle triangle;
    
    private Pencil pencil;
    private Bucket bucket;
    private Curve curve;
    private Eraser eraser;
    
    private int delay = 30;
    private boolean isPlaying;
    private boolean replay;
    
    private PaintTool paintTool;
    private JToggleButton bPlay;
    
    private Thread thread = null;
    
    private int currentState = 0;
    private int currentStep = 0;
    private int cStateElement = 0;
    
    private ArrayList<Point> listPoint;
    private ArrayList<DrawType> listState;
    private ArrayList<Integer> listDrawStep;
    
    private Graphics2D g2d, g2;
    private SelectionShape selectedRect;
    
    private Point curvePoint1;
    private Point curvePoint2;
    private Point curvePoint3;
    
    public ReplayPanel() {
        initComponents();
        
        listState = new ArrayList<>();
        listDrawStep = new ArrayList<>();
        
        paintTool = new PaintTool();
        paintState = new PaintState();
        
        line = new Line();
        rect = new Rectangle();
        oval = new Oval();
        pencil = new Pencil();
        triangle = new Triangle();
        bucket = new Bucket();
        curve = new Curve();
        eraser = new Eraser();
        
        isPlaying = false;
        
        org_img = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB);
        buff_img = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB);
        
        g2 = (Graphics2D) org_img.getGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getSize().width, getSize().height);
        g2.dispose();
        
        
        // Lay trang thai ban cua State
        readState();
    }
    
     /**
     * Doc cac trang thai va cac diem da duoc ve trong trang thai do tu cac list.
     * listPoint la danh sach cac diem da ve.
     * listState la danh sach lu cac trang thai cua cac buoc ve.
     * DrawStepList la danh sach cho biet hien tai dang o buoc ve nao.
     */
    
    public void readState(){
        listPoint = new ArrayList<>();
        listState = paintState.getListState();
        listDrawStep = paintState.getDrawStepList();
        
        org_img.flush();
        buff_img.flush();
        
        System.gc();
        
        org_img = null;
        buff_img = null; 
        
        int w = paintState.getWidth();
        int h = paintState.getHeight();
        
        int[] data = paintState.getData();
        
        org_img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        org_img.getRaster().setPixels(0, 0, w, h, data);
        buff_img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        
        g2d = (Graphics2D) buff_img.getGraphics();
        g2d.drawImage(org_img, 0, 0, null);
        
        this.setSize(new Dimension(w, h));
        this.setPreferredSize(new Dimension(w, h));
        this.setMinimumSize(new Dimension(w, h));
        this.revalidate();
    }
    
    public void setButton(JToggleButton bPlay){
        this.bPlay = bPlay;
    }
    
    public void setDelay(int delay){
        this.delay = (105 - delay)/2;
    }
    
    //Duy tri qua trinh ve hien tai
    
    public void drawRemainderImage(){
        Graphics2D g2dBuffer = (Graphics2D) buff_img.getGraphics();
        int shapeIndex = 0;
        
        for(int i = this.currentStep - 1 ; i < paintState.getDrawStepList().size() ; i++){
            int inStepState = paintState.getDrawStepList().get(i);
            
            DrawType inDrawType = paintState.getListState().get(shapeIndex);
            
            if(inDrawType instanceof Line){
                Line inLine = (Line) inDrawType;
                inLine.draw(g2d);
            }else if(inDrawType instanceof Triangle){
                Triangle inTriangle = (Triangle) inDrawType;
                inTriangle.draw(g2d);
            }else if(inDrawType instanceof Rectangle){
                Rectangle inRectangle = (Rectangle) inDrawType;
                inRectangle.draw(g2d);
            }else if(inDrawType instanceof Oval){
                Oval inOval = (Oval) inDrawType;
                inOval.draw(g2d);
            }else if(inDrawType instanceof SelectionShape){
                SelectionShape inSelectedRect = (SelectionShape) inDrawType;
                inSelectedRect.draw(g2d);
            }else if(inDrawType instanceof Pencil){
                Pencil inPencil = (Pencil) inDrawType;
                for(int j = 1 ; j < inPencil.getDraggedPoint().size() ; j++){
                    inPencil.setPoint(inPencil.getDraggedPoint().get(j-1), inPencil.getDraggedPoint().get(j));
                    inPencil.draw(g2d);
                }
            }else if(inDrawType instanceof Eraser){
                Eraser inEraser = (Eraser) inDrawType;
                for(int j = 1 ; j < inEraser.getDraggedPoint().size() ; j++){
                    inEraser.setPoint(inEraser.getDraggedPoint().get(j-1), inEraser.getDraggedPoint().get(j));
                    inEraser.draw(g2d);
                }
            }else if(inDrawType instanceof Bucket){
                bucket = (Bucket) inDrawType;
                listPoint = bucket.getArrPoint();
                Bucket inBucket = (Bucket) inDrawType;
                for(int j = 1 ; j < inBucket.getDraggedPoint().size() ; j++){
                    inBucket.setStart(inBucket.getArrPoint().get(j-1));
                    inBucket.draw(buff_img);
                }
            }
            shapeIndex++;
            break;
        }
        repaint();
    }
    
    
    
    public void startReplay(){
        if(thread == null){
            refresh();
            thread = new Thread(this);
            isPlaying = true;
            thread.start();
        }
        
        isPlaying = true;
    }
    
    public void pauseReplay(){
        isPlaying = false;
    }
    
    public void stopReplay(){
        drawRemainderImage();
        currentStep = this.listDrawStep.size();
        currentState = listState.size();
        System.gc();
    }
    
    
    //Xoa trang thai ghi hinh
    public void flush(){
        if(isPlaying){
            bPlay.setIcon(new ImageIcon(getClass().getResource("/data/icon/pause.png")));
            isPlaying = false;
        }
                
        paintState = new PaintState();
        org_img = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB);
        g2 = (Graphics2D) org_img.getGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getSize().width, getSize().height);
        g2.dispose();
        paintState.setData(org_img);
    }
    
    public boolean isPlaying(){
        return isPlaying;
    }
    
    public void setPaintState(PaintState paintState){
        this.paintState = paintState;
        readState();
    }
    
    
    //Refresh lai qua trinh replay ve trang thai ban dau
    public void refresh(){
        g2 = (Graphics2D) buff_img.getGraphics();
        g2.drawImage(org_img, 0, 0, null);
        g2.dispose();
        
        currentState = 0;
        currentStep = 0;
        cStateElement = 0;
        
        repaint();
    }
    
    //Huy bo cac doi tuong khi khong replay nua
    public void dispose(){
        org_img = null;
        buff_img = null;
        line = null;
        rect = null;
        oval = null;
        eraser = null;
        pencil = null;
        bucket = null;
        curve = null;
        selectedRect = null;
        triangle = null;
        g2.dispose();
        g2d.dispose();
        System.gc();
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        g.drawImage(buff_img, 0, 0, null);
        
        if(isPlaying){
            Graphics2D g2dBuffer = (Graphics2D) buff_img.getGraphics();
            if(currentState < listState.size() && listPoint != null && cStateElement < listPoint.size()){
                DrawType drawType = listState.get(currentState);
                
                if(drawType instanceof Line){
                    line.setPoint(listPoint.get(0), listPoint.get(cStateElement));
                    line.draw(g2);
                    
                    //Neu dat trang thai cuoi thi ve len buffer
                    if(cStateElement == listPoint.size() - 1){
                        line.draw(g2dBuffer);
                    }
                }else if(drawType instanceof Rectangle){
                    rect.setPoint(listPoint.get(0), listPoint.get(cStateElement));
                    rect.draw(g2);
                    if(cStateElement == listPoint.size() - 1){
                        rect.draw(g2dBuffer);
                    }
                }else if(drawType instanceof Triangle){
                    triangle.setPoint(listPoint.get(0), listPoint.get(cStateElement));
                    triangle.draw(g2);
                    if(cStateElement == listPoint.size() - 1){
                        triangle.draw(g2dBuffer);
                    }
                }else if(drawType instanceof Oval){
                    oval.setPoint(listPoint.get(0), listPoint.get(cStateElement));
                    oval.draw(g2);
                    if(cStateElement == listPoint.size() - 1){
                        oval.draw(g2dBuffer);
                    }
                }else if(drawType instanceof Curve){
                    int state = curve.getState();
                    ArrayList<Point> list = new ArrayList<>();
                    
                    Point start = curve.getStart();
                    
                    if(state == 1){
                        curvePoint3 = listPoint.get(cStateElement);
                        curvePoint1.setLocation(curvePoint3);
                        curvePoint2.setLocation(curvePoint3);
                        
                        if(cStateElement == curve.getSizeOfStateFirst() - 1){
                            curvePoint3 = listPoint.get(cStateElement);
                            curve.setState(2);
                        }
                    }else if(state == 2){
                        curvePoint2 = listPoint.get(cStateElement);
                        curvePoint1.setLocation(curvePoint2);
                        
                        if(cStateElement == curve.getSizeOfStateSecond() - 1){
                            curve.setState(3);
                            curvePoint2 = listPoint.get(cStateElement);
                        }
                    }else if(state == 3){
                        curvePoint1 = listPoint.get(cStateElement);
                    }
                    list.add(start);
                    list.add(curvePoint2);
                    list.add(curvePoint1);
                    list.add(curvePoint3);
                    
                    curve.setList(list);
                    curve.draw(g2);
                    if(cStateElement == listPoint.size() - 1){
                        curve.draw(g2dBuffer);
                    }
                }else if(drawType instanceof SelectionShape){
                    if(cStateElement == 0){
                        return;
                    }
                    selectedRect.setStart(listPoint.get(cStateElement));
                    selectedRect.draw(g2);
                    if(cStateElement == listPoint.size() - 1){
                        selectedRect.draw(g2dBuffer);
                    }
                }else if(drawType instanceof Pencil){
                    if(cStateElement < listPoint.size() - 1){
                        pencil.setPoint(listPoint.get(0), listPoint.get(cStateElement));
                        pencil.draw(g2dBuffer);
                    }
                }else if(drawType instanceof Eraser){
                    eraser.setPoint(listPoint.get(0), listPoint.get(cStateElement));
                    eraser.draw(g2);
                    if(cStateElement == listPoint.size() - 1){
                        eraser.draw(g2dBuffer);
                    }
                }
               
            }
        }
    }
    
    public BufferedImage getBuffer(){
        return buff_img;
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void run() {
        int increDelay = 60;
        
        while(currentStep < paintState.getDrawStepList().size()){
            if(isPlaying == false){
                continue;
            }
            
            //int inStepState = listDrawStep.get(currentStep);
            
            if(listPoint == null){
                DrawType inDrawType = listState.get(currentState);
                
                if(inDrawType instanceof Line){
                    line = (Line) inDrawType;
                    listPoint = line.getDraggedPoint();
                }else if(inDrawType instanceof Triangle){
                    triangle = (Triangle) inDrawType;
                    listPoint = triangle.getDraggedPoint();
                }else if(inDrawType instanceof Rectangle){
                    rect = (Rectangle) inDrawType;
                    listPoint = rect.getDraggedPoint();
                }else if(inDrawType instanceof Oval){
                    oval = (Oval) inDrawType;
                    listPoint = oval.getDraggedPoint();
                }else if(inDrawType instanceof Curve){
                    curve = (Curve) inDrawType;
                    curve.resetState();
                    listPoint = curve.getDraggedPoint();
                }else if(inDrawType instanceof Pencil){
                    pencil = (Pencil) inDrawType;
                    listPoint = pencil.getDraggedPoint();
                }else if(inDrawType instanceof SelectionShape){
                    selectedRect = (SelectionShape) inDrawType;
                    listPoint = selectedRect.getDraggedPoint();
                }else if(inDrawType instanceof Eraser){
                    eraser = (Eraser) inDrawType;
                    listPoint = eraser.getDraggedPoint();
                    Eraser inEraser = (Eraser) inDrawType;
                    for(int j = 1 ; j < inEraser.getDraggedPoint().size() ; j++){
                        inEraser.setPoint(inEraser.getDraggedPoint().get(j-1), inEraser.getDraggedPoint().get(j));
                        inEraser.draw(g2d);
                    }
                }else if(inDrawType instanceof Bucket){
                    bucket = (Bucket) inDrawType;
                    listPoint = bucket.getArrPoint();
                    Bucket inBucket = (Bucket) inDrawType;
                    for(int j = 1 ; j < inBucket.getArrPoint().size() ; j++){
                        inBucket.setStart(inBucket.getArrPoint().get(j-1));
                        inBucket.draw(g2d);
                    }
                }
            }
            //Neu diem da duoc khoi tao 
            else{
                //Kiem tra da den trang thai cuoi cung chua
                if(cStateElement == listPoint.size()){
                    listPoint = null;
                    cStateElement = 0;
                    currentState++;
                    currentStep++;
                }
                /*
                Neu van con trang thai cho viec ve hinh va trong danh sach cac diem cua hinh hien tai
                diem con chua phai diem cuoi cung thi se tang trang thai cua diem hien tai len 1
                */
                else{
                    ++cStateElement;
                }
            }
            
            try{
                Thread.sleep(delay);
            }catch(InterruptedException e){
                System.out.println("error in replayPanel in run() method");
            }
            repaint();
        }
        
        System.gc();
        isPlaying = false;
        thread = null;
        replay = false;
        bPlay.setIcon(new ImageIcon(getClass().getResource("/data/icon/pause.png")));
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
