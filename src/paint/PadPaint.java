/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import paint.PaintTool.DrawMode;
import property.ColorDialog;
import property.StrokeState;
import property.TextPanel;
import shape.Bucket;
import shape.Curve;
import shape.DrawType;
import shape.Eraser;
import shape.Line;
import shape.Oval;
import shape.Pencil;
import shape.Picker;
import shape.Rectangle;
import shape.SelectionShape;
import shape.Text;
import shape.Triangle;

/**
 *
 * @author Vu
 */
public class PadPaint extends javax.swing.JPanel implements MouseListener, MouseMotionListener {
    
    //Shape objects
    private PaintTool paintTool = new PaintTool();
    private ColorDialog colorChooser = new ColorDialog();
    private Line line;
    private Rectangle rect;
    private Oval oval;
    private Pencil pencil;
    private Eraser eraser;
    private Curve curve;
    private Triangle triangle;
    private Bucket bucket;
    private Picker picker;
    private SelectionShape select_;
    private Point locationEraser = new Point();
    private Text text;
    private TextPanel textPanel = new TextPanel();
    private Cursor cursor;
    private Cursor cursorOfPaint;
    private Cursor cursorOfPicker;
    private Cursor cursorOfEraser;
    private Cursor cursorOfBucket;
    private Cursor cursorOfZoom;
    //PadPaint Var
    private StrokeState strokeState = new StrokeState();
    private PaintState paintState = new PaintState();
    private PaintState redoState = new PaintState();
    private BufferedImage buff_img,org_img,cpy_img;
    private Point start,end,temp;
    private Graphics2D g2d, g2;
    private Color strokeColor = Color.BLACK;
    private Color fillColor = Color.WHITE;
    private int width = 0;
    private int height = 0;
    private boolean isSaved = true;
    private double zoom = 1;
    private JLabel lbLocation;
    private JLabel lbSize;
    private int temp1;
    private int temp2;

    private boolean draggingMouse = false;
    private boolean startCurve;
    private boolean isMouseExit;
    private boolean startSelRect = false;
    //setup cac thuoc tinh co ban
    public void setTextPanel(TextPanel textPanel){
        this.textPanel = textPanel;
    } 
    public void setStrokeState(StrokeState strokeState){
        this.strokeState = strokeState;
    } 
    
    public void setColorChooser(ColorDialog colorChooser) {
        this.colorChooser = colorChooser;
    }
    //Tao cai paintState de luu
    public PaintState getListState() {
        return paintState;
    }
    
    
    
    public Cursor setCursor(String path, String nameCursor, int x, int y){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(getClass().getResource(path));
        
        Point point = new Point(x,y);
        Cursor cursor = toolkit.createCustomCursor(image, point, nameCursor);
        return cursor;
    }// dat cursor neu minh khong co thi bo di k set cx dc
    
     public boolean isSaving() {
        return isSaved;
    }
    
    public void setZoom(int z){
        
    }
    
    public double getzoom(){
        return zoom;
    }
    
    public Point getPoint(Point location){
        if(location == null){
            return null;
        }
        Point p = new Point((int) (location.x/zoom),(int) (location.y/zoom));
        return p;
    }// Dùng điểm đang trỏ vào chia cho biến zoom để lấy giá trị thực
    
    public PadPaint(int width, int height){
        //Khởi tạo các shape cơ bản
        initComponents();
        line = new Line();
        rect = new Rectangle();
        oval = new Oval();
        triangle = new Triangle();
        pencil = new Pencil();
        start = new Point(-1,-1);
        end = new Point(-1,-1);
        this.width = width;
        this.height = height;
        this.setSize(new Dimension(width,height));
        
        //Tao anh goc
        org_img = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
        g2d = (Graphics2D) org_img.getGraphics();
        g2d.setColor(new Color(255,255,255));
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        //Tao hinh anh buffer
        buff_img = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
        g2d = (Graphics2D) buff_img.getGraphics();
        g2d.setColor(new Color(255,255,255));
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        
        paintState.setData(org_img);
        initState();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }
    
    public void cut(){
        if(select_ != null){
            int[] data = select_.getData();// lấy data trong select ở đây là 1 mảng pixel
            int w = select_.getWidth();
            int h = select_.getHeight();
            if(w == 0 || h == 0){
                return;
            }
            cpy_img = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);//ghi phần select vào cpy_img
            cpy_img.getRaster().setPixels(0, 0, w, h, data);
            Graphics2D g = (Graphics2D) buff_img.getGraphics();//Gán đối tượng g cho buff_img
            g.setColor(Color.WHITE);
            g.fillRect(select_.getStartOrigin().x, select_.getStartOrigin().y, w, h);//tô trằng cho phần select
            repaint();//vẽ lại lên màn hình
            select_ = null;//bỏ phần select đi
            g.dispose();//xóa cái g
        }
    }
    
    public void copy(){
        if(select_ != null){
            int[] data = select_.getData();
            int w = select_.getWidth();
            int h = select_.getHeight();
            if(w == 0 || h == 0){
                return;
            }
            cpy_img = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
            cpy_img.getRaster().setPixels(0, 0, w, h, data);
        }
    }
    
    public void paste(){// vẽ cpy_img lên chỗ
        if(cpy_img != null){
            Graphics2D g = (Graphics2D) buff_img.getGraphics();
            g.drawImage(cpy_img,temp.x, temp.y, null);
            g.dispose();
        }
        repaint();
        cpy_img = null;
        select_ = null;    
    }
    
    public void detele(){
        if(select_ != null){
            int[] data = select_.getData();// lấy data trong select ở đây là 1 mảng pixel
            int w = select_.getWidth();
            int h = select_.getHeight();
            if(w == 0 || h == 0){
                return;
            }
            Graphics2D g = (Graphics2D) buff_img.getGraphics();//Gán đối tượng g cho buff_img
            g.setColor(Color.WHITE);
            g.fillRect(select_.getStartOrigin().x, select_.getStartOrigin().y, w, h);//tô trằng cho phần select
            repaint();//vẽ lại lên màn hình
            select_ = null;//bỏ phần select đi
            g.dispose();//xóa cái g
        }
    }
    
    public void initState(){
        Pencil pencil = new Pencil();
        pencil.addDraggedPoint(new Point(-1,-1));
        pencil.addDraggedPoint(new Point(-1,-1));
        pencil.setPoint(new Point(-1,-1), new Point(-1,-1));
        BasicStroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                1.0f, null, 2.0f);
        paintState.addDrawState(pencil);
        paintState.addDrawStep(PaintState.PAINTTING);
        //khởi tạo tool ban đầu khi mở ctr
    }
    
    @SuppressWarnings("unchecked")
    public void initComponents() {// taoj doi tuong va set layout
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
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        if(buff_img == null){// nếu chưa có ảnh thì khởi tạo
            buff_img = (BufferedImage) createImage(getSize().width,getSize().height);
            g2d = (Graphics2D) buff_img.getGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            refresh();
        }
        setSizeStatusInfo();
        g2.scale(zoom, zoom);
        g2.drawImage(buff_img, null, 0, 0);
        if(start != null && end != null){
            switch(paintTool.getDrawMode()){
                
            }
        }
    }
    
    public BufferedImage getBuffer() {
        return buff_img;
    }

    public void refresh() {
        //Trong qua trinh ve anh goc can co cac tham so de chi ra duoc anh goc bi quay tai mot buoc ve nao do
        g2d.drawImage(org_img, 0, 0, this);
        repaint();
    }
    
    public void addDrawStep(int drawStep){
        paintState.addDrawStep(drawStep);
    }
    
    
    public void setLocationStatus(JLabel lbLocation) {
        this.lbLocation = lbLocation;
    }

    public void setSizeStatus(JLabel lbSize) {
        this.lbSize = lbSize;
    }

    public void setSizeStatusInfo() {
        lbSize.setText(buff_img.getWidth() + ", " + buff_img.getHeight() + "px");
    }
    
    public void flush(){// xóa mọi thứ và trả về trạng thái ban đầu
        start = null;
        end = null;
        curve= null;
        paintState.removeAll();
        initState();
        org_img.flush();
        buff_img.flush();
        redoState.removeAll();
        System.gc();
        org_img = null;
        buff_img = null;
        org_img = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getSize().width, getSize().height);
        g2.dispose();
        paintState.setData(org_img);
        isSaved = true;
        refresh();
        repaint();
    }
    //MouseOverride
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    
    }
    @Override
    public void mousePressed(MouseEvent e){
        
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        
    }
}
