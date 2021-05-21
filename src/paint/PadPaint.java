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
    private SelectionShape sel_rect;//select rectangle
    private Point locationEraser = new Point();
    private Text text;
    private TextPanel textPanel = new TextPanel();
    private Cursor cursor;
    private Cursor cursorOfPaint;
    private Cursor cursorOfPencil;
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
    public void setPaintTool(PaintTool paintTool) {
        this.paintTool = paintTool;
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
     
    public void saveImage(File f, String extension){
        try{
            ImageIO.write(buff_img, extension, f);
            isSaved = true;
        }catch(IOException e){
            isSaved = false;
            System.out.println("Error in saveImage() in padpaint");
        }
    }
    
    public void loadImage(BufferedImage buff_img){
        //Khi anh moi duoc mo thi khong phai luu
        isSaved = true;
        zoom = 1;
        loadImage((Image) buff_img);
    }
    
    public void loadImage(Image img){
        if(img != null){
            flush();
            org_img = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            g2 = (Graphics2D) org_img.getGraphics();
            g2.drawImage(img, 0, 0, img.getWidth(null), img.getHeight(null), this);
            g2.dispose();
            
            buff_img = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            g2 = (Graphics2D) buff_img.getGraphics();
            g2.drawImage(img, 0, 0, img.getWidth(null), img.getHeight(null), this);
            g2.dispose();
            
            paintState.setData(org_img);
            g2d = (Graphics2D) buff_img.getGraphics();
            
            this.setSize(new Dimension(org_img.getWidth(), org_img.getHeight()));
            this.setMinimumSize(new Dimension(org_img.getWidth(), org_img.getHeight()));
            this.setMaximumSize(new Dimension(org_img.getWidth(), org_img.getHeight(null)));
            this.revalidate();
            repaint();
        }
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
        bucket = new Bucket();
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
        cursorOfEraser = setCursor("/paint/eraser.png", "eraser", 16, 16);
        cursorOfPaint = setCursor("/paint/paint2.png", "paint2", 15, 15);
        cursorOfBucket = setCursor("/paint/bucket.png", "bucket", 5, 4);
        cursorOfPicker = setCursor("/paint/picker.png", "picker", 7, 22);
        cursorOfPencil = setCursor("/paint/pen.png", "pencil", 16, 16);

    }
    
    private boolean testHit(Point p) {//test bấm xem có bấm vào trong phần select k
        if (sel_rect != null && sel_rect.getStartLocation() != null && sel_rect.getEndlocation() != null) {

            if (p.x < Math.min(sel_rect.getStartLocation().x, sel_rect.getEndlocation().x)
                    || p.x > Math.max(sel_rect.getStartLocation().x, sel_rect.getEndlocation().x)) {
                return false;
            } else if (p.y < Math.min(sel_rect.getStartLocation().y, sel_rect.getEndlocation().y)
                    || p.y > Math.max(sel_rect.getStartLocation().y, sel_rect.getEndlocation().y)) {

                return false;
            }
            return true;
        } else {
            return false;
        }
    }
    
    private boolean testMousePressed(Point p, Point start, Point end) {
        int a[] = {Math.min(start.x, end.x), Math.min(start.y, end.y), Math.max(start.x, end.x), Math.max(start.y, end.y)};
        if (p.x > a[0] && p.x < a[2] && p.y > a[1] && p.y < a[3]) {
            return true;
        } else {
            return false;
        }
    }
    
    public void cut(){
        if(sel_rect != null){
            int[] data = sel_rect.getData();// lấy data trong select ở đây là 1 mảng pixel
            int w = sel_rect.getWidth();
            int h = sel_rect.getHeight();
            if(w == 0 || h == 0){
                return;
            }
            cpy_img = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);//ghi phần select vào cpy_img
            cpy_img.getRaster().setPixels(0, 0, w, h, data);
            Graphics2D g = (Graphics2D) buff_img.getGraphics();//Gán đối tượng g cho buff_img
            g.setColor(Color.WHITE);
            g.fillRect(sel_rect.getStartOrigin().x, sel_rect.getStartOrigin().y, w, h);//tô trằng cho phần select
            repaint();//vẽ lại lên màn hình
            sel_rect = null;//bỏ phần select đi
            g.dispose();//xóa cái g
        }
    }
    
    public void copy(){
        if(sel_rect != null){
            int[] data = sel_rect.getData();
            int w = sel_rect.getWidth();
            int h = sel_rect.getHeight();
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
        sel_rect = null;    
    }
    
    public void delete(){
        if(sel_rect != null){
            int[] data = sel_rect.getData();// lấy data trong select ở đây là 1 mảng pixel
            int w = sel_rect.getWidth();
            int h = sel_rect.getHeight();
            if(w == 0 || h == 0){
                return;
            }
            Graphics2D g = (Graphics2D) buff_img.getGraphics();//Gán đối tượng g cho buff_img
            g.setColor(Color.WHITE);
            g.fillRect(sel_rect.getStartOrigin().x, sel_rect.getStartOrigin().y, w, h);//tô trằng cho phần select
            repaint();//vẽ lại lên màn hình
            sel_rect = null;//bỏ phần select đi
            g.dispose();//xóa cái g
        }
    }
    
    public void undo() {
        toolChange();
        if (paintState.isEmpty()) {
            return;
        }
        //Lay ra trang thai cuoi de tro lai trang thai truoc do
        int stepState = paintState.removeEndStep();
        redoState.addDrawStep(stepState);
        //lay ra trang thai cua buoc ve cuoi cung
        switch (stepState) {
            case PaintState.PAINTTING:
                //Neu la painting thi ve lai tu dau den trang thai truoc do
                //Ve lai anh goc
                //khoi tao mot buffer moi de ve len panel
                buff_img = new BufferedImage(org_img.getWidth(), org_img.getHeight(), BufferedImage.TYPE_INT_RGB);
                g2d = (Graphics2D) buff_img.getGraphics();
                refresh();
                //Xoa trang thai cuoi
                DrawType drawType = paintState.removeEndShape();
                redoState.addDrawState(drawType);
                //Ve lai toan bo trang thai cua anh tu luc dau den luc 
                int shapeIndex = 0;
                for (int i = 0; i < paintState.getDrawStepList().size(); i++) {
                    int inStepState = paintState.getDrawStepList().get(i);
                    //Lay tung trang thia cua buoc ve
                    switch (inStepState) {
                        case PaintState.PAINTTING:
                            //Neu la painting thi se ve lai toan bo anh tu dau
                            DrawType inDrawType = paintState.getListState().get(shapeIndex);
                            if (inDrawType instanceof Line) {
                                Line inLine = (Line) inDrawType;
                                inLine.draw(g2d);
                            } else if (inDrawType instanceof Triangle) {
                                Triangle inTriangle = (Triangle) inDrawType;
                                inTriangle.draw(g2d);
                            } else if (inDrawType instanceof Rectangle) {
                                Rectangle inRect = (Rectangle) inDrawType;
                                inRect.draw(g2d);
                            
                            } else if (inDrawType instanceof Oval) {
                                Oval inOval = (Oval) inDrawType;
                                inOval.draw(g2d);
                            } else if (inDrawType instanceof Curve) {
                                Curve inCurve = (Curve) inDrawType;
                                inCurve.draw(g2d);

                            } else if (inDrawType instanceof SelectionShape) {
                                SelectionShape inselrect = (SelectionShape) inDrawType;
                                inselrect.draw(g2d);
                            } else if (inDrawType instanceof Pencil) {
                                Pencil inPencil = (Pencil) inDrawType;
                                for (int j = 1; j < inPencil.getDraggedPoint().size(); j++) {
                                    inPencil.setPoint(inPencil.getDraggedPoint().get(j - 1), inPencil.getDraggedPoint().get(j));
                                    inPencil.draw(g2d);
                                }

                            } //update by Khanh
                            else if (inDrawType instanceof Bucket) {
                                Bucket inBucket = (Bucket) inDrawType;
                                inBucket.draw(buff_img);
                            }
                            shapeIndex++;
                            break;
                    }
                }

                break;
        }

        repaint();
    }
    
    public void redo() {
        if (!redoState.isEmpty()) {
            int stepState = redoState.removeEndStep();
            paintState.addDrawStep(stepState);
            DrawType drawType0 = redoState.removeEndShape();
            paintState.addDrawState(drawType0);
            buff_img = new BufferedImage(org_img.getWidth(), org_img.getHeight(), BufferedImage.TYPE_INT_RGB);
            g2d = (Graphics2D) buff_img.getGraphics();
            refresh();
            //Ve lai trang thai truoc do
            int shapeIndex = 0;
            for (int i = 0; i < paintState.getDrawStepList().size(); i++) {
                int inStepState = paintState.getDrawStepList().get(i);
                //Lay tung trang thia cua buoc ve
                switch (inStepState) {
                    
                    case PaintState.PAINTTING:
                        DrawType inDrawType = paintState.getListState().get(shapeIndex);
                        if (inDrawType instanceof Line) {
                            Line inLine = (Line) inDrawType;
                            inLine.draw(g2d);
                        } else if (inDrawType instanceof Triangle) {
                            Triangle inTriangle = (Triangle) inDrawType;
                            inTriangle.draw(g2d);
                        } else if (inDrawType instanceof Rectangle) {
                            Rectangle inRect = (Rectangle) inDrawType;
                            inRect.draw(g2d);
                        } else if (inDrawType instanceof Oval) {
                            Oval inOval = (Oval) inDrawType;
                            inOval.draw(g2d);

                        } else if (inDrawType instanceof SelectionShape) {
                            SelectionShape inselrect = (SelectionShape) inDrawType;
                            inselrect.draw(g2d);
                        } else if (inDrawType instanceof Curve) {
                            Curve inCurve = (Curve) inDrawType;
                            inCurve.draw(g2d);
                        } else if (inDrawType instanceof Pencil) {
                            Pencil inPencil = (Pencil) inDrawType;
                            for (int j = 1; j < inPencil.getDraggedPoint().size(); j++) {
                                inPencil.setPoint(inPencil.getDraggedPoint().get(j - 1), inPencil.getDraggedPoint().get(j));
                                inPencil.draw(g2d);
                            }

                        } //update by Khanh
                        else if (inDrawType instanceof Bucket) {
                            Bucket inBucket = (Bucket) inDrawType;
                            inBucket.draw(buff_img);
                        }
                        shapeIndex++;
                }

                repaint();
            }
        }
    }
    
    public void toolChange(){//Khi chuyen sang tool khac ma van con tool dang dung
        if(startCurve == true){//Neu chon curve thi khong sang tool khac ma chi them vao mảng
            if(paintTool.getDrawMode() == DrawMode.CURVE){
                curve.draw(g2d);
                paintState.addDrawState(curve);
                paintState.addDrawStep(PaintState.PAINTTING);
                curve = null;
                startCurve = false;
                start = null;
                end = null;
            }
            else{
                curve.draw(g2d);
                repaint();
                paintState.addDrawState(curve);
                paintState.addDrawStep(PaintState.PAINTTING);
                curve = null;
                startCurve = false;
                start = null;
                end = null;
            }
        } else if(startSelRect == true){ // dang select
            if(sel_rect.isCreating()){// neu ma dang dc chon thi luu anh
                sel_rect.setSelected(true);
                sel_rect.draw(g2d);
                paintState.addDrawState(sel_rect);
                paintState.addDrawStep(PaintState.PAINTTING);
                sel_rect = null;
                startSelRect = false;
                start = null;
                end = null;
            }
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
                case LINE:
                    line.draw(g2);
                    break;
                case RECTANGLE:
                    rect.draw(g2);
                    break;
                case TRIANGLE:
                    triangle.draw(g2);
                    break;
                case BUCKET:
                    bucket.draw(buff_img);
                    break;
                case OVAL:
                    oval.draw(g2);
                    break;
                case PENCIL:
                    pencil.draw(g2);
                    break;
                case CURVE:
                    if(curve != null){
                        curve.draw(g2);
                    }
                    break;
                case SELECT:
                    if(sel_rect != null){
                        sel_rect.draw(g2);
                    }
                case TEXT:
                    if(text != null){
                        text.draw(g2, g2d);
                    }
                    break;
                
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
        g2 = (Graphics2D) org_img.getGraphics();
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
        lbLocation.setText("");
    }
    @Override
    public void mousePressed(MouseEvent e){
        
        // xoa redo stack
        if(!redoState.isEmpty()){
            redoState.removeAll();
        }
        start = e.getPoint();
        temp = e.getPoint();
        
        switch(paintTool.getDrawMode()){//xet qua cac trang thai
            case LINE:
                line = new Line();
                line.setStroke(strokeState.getStroke());
                //set màu cho stroke
                line.setStrokeColor(colorChooser.getStrokeColor());
                //Thêm điểm đầu vào danh sách điểm di chuột
                line.addDraggedPoint(getPoint(start));
                //Thêm điểm vào để vẽ
                line.setPoint(getPoint(start), getPoint(start));
            case RECTANGLE:
                rect = new Rectangle();
                rect.setStroke(strokeState.getStroke());
                rect.setStrokeColor(colorChooser.getStrokeColor());
                rect.setFillColor(colorChooser.getFillColor());
                rect.addDraggedPoint(getPoint(start));
                rect.setPoint(getPoint(start), getPoint(start));
                break;
            case TRIANGLE:
                triangle = new Triangle();
                triangle.setStroke(strokeState.getStroke());
                triangle.setStrokeColor(colorChooser.getStrokeColor());
                triangle.setFillColor(colorChooser.getFillColor());
                triangle.addDraggedPoint(getPoint(start));
                triangle.setPoint(getPoint(start), getPoint(start));
                break;
            case OVAL:
                oval = new Oval();
                oval.setStroke(strokeState.getStroke());
                oval.setStrokeColor(colorChooser.getStrokeColor());
                oval.setFillColor(colorChooser.getFillColor());
                oval.setPoint(getPoint(start), getPoint(start));
                oval.addDraggedPoint(getPoint(start));
                break;
            case PENCIL:
                pencil = new Pencil();
                pencil.setStroke(strokeState.getStroke());
                pencil.setStrokeColor(colorChooser.getStrokeColor());
                pencil.setPoint(getPoint(start), getPoint(start));
                pencil.addDraggedPoint(getPoint(start));
                pencil.draw(g2d);
                break;
            case BUCKET:
                bucket = new Bucket();
                bucket.setStart(getPoint(start));
                bucket.setArrPoint(getPoint(start));
                bucket.setColor(colorChooser.getFillColor());
                bucket.draw(buff_img);
                paintState.addDrawState(bucket);
                break;
            case ERASER:
                eraser = new Eraser();
                eraser.setStroke(strokeState.getStroke());
                eraser.setStrokeColor(colorChooser.getFillColor());
                eraser.setPoint(getPoint(start), getPoint(start));
                eraser.addDraggedPoint(getPoint(start));
                eraser.draw(g2d);
                locationEraser.move((int) (e.getPoint().x / zoom), (int) (e.getPoint().y / zoom));
                break;
            case CURVE:
                if(curve == null){// neu curve chua tao thi tao cai moi
                    curve = new Curve();
                    startCurve = true;//danh dau dang curve
                    curve.setStroke(strokeState.getStroke());
                    // gan cac dac tinh hien tai cho curve la do to stroke mau stroke va diem bat dau
                    curve.setStrokeColor(colorChooser.getStrokeColor());
                    curve.setStartPoint(getPoint(start));
                }
                if (curve.getState() == 1) {
                    //Neu cung chi vua moi duoc tao thi se set lai diem dau tien trong danh sach diem
                    curve.getList().get(0).setLocation(getPoint(e.getPoint()));
                    //Them diem vao danh sach
                } else if (curve.getState() == 2) {
                    curve.getList().get(1).setLocation(getPoint(e.getPoint()));

                } else if (curve.getState() == 3) {
                    curve.getList().get(2).setLocation(getPoint(e.getPoint()));
                }
                curve.addDraggedPoint(getPoint(start));
                curve.addPointToState(getPoint(start));
                break;
            case SELECT:
                if(sel_rect != null){
                    if(sel_rect.isCreating()){// Nếu đã được tạo rồi
                        if(!testHit(getPoint(getPoint(start)))){// Bấm ra ngoài thì xóa
                            sel_rect.setSelected(true);
                            end = null;
                            start = null;
                            sel_rect.draw(g2d);
                            paintState.addDrawState(sel_rect);
                            paintState.addDrawStep(PaintState.PAINTTING);
                            repaint();
                            sel_rect = null;
                            startSelRect = false;
                        }
                        else{
                            temp1 = sel_rect.getStartLocation().x - getPoint(start).x;
                            temp2 = sel_rect.getStartLocation().y - getPoint(start).y;
                            sel_rect.addDraggedPoint(getPoint(new Point(start.x - temp1, start.y - temp2)));
                            return;
                        }
                    }
                }
                if(sel_rect == null){// neu chua tao thi them diem vao
                    sel_rect = new SelectionShape();
                    startSelRect = true;//danh dau la dang select
                    //them vao toa do 
                    start = e.getPoint();
                    sel_rect.setStartOrigin(getPoint(start));
                    sel_rect.setStart(getPoint(start));
                }
                break;
            case TEXT:
                if(text != null){
                    text.setIsCreated(true);
                    if(testMousePressed(text.getStart(), text.getEnd(), e.getPoint()) == false){
                        if(text.checkOverlap() == false){
                            text.setString();
                            if (text.getString().equals("") == false) {// nếu có viết vào thì vẽ lại lên màn 
                                repaint();
                            }
                        }
                        text.removeArea(this);// xóa vùng 
                    }
                    return;
                }
                else{// chưa có thì tạo 
                    text = new Text();
                    text.setStart(e.getPoint());
                    text.setIsCreated(false);
                    return;
                }
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        switch(paintTool.getDrawMode()){// đền khi thả tay ra thì lưu trạng thái
            case LINE:
                paintState.addDrawState(line);
                line.draw((g2d));
                break;
            case RECTANGLE:
                paintState.addDrawState(rect);
                rect.draw(g2d);
                break;
            case TRIANGLE:
                paintState.addDrawState(triangle);
                triangle.draw(g2d);
                break;
            case OVAL:
                paintState.addDrawState(oval);
                oval.draw(g2d);
                break;
            case ERASER:
                paintState.addDrawState(eraser);
                eraser.draw(g2d);
                locationEraser.move((int) (e.getPoint().x / zoom), (int) (e.getPoint().y / zoom));
                break;
            case PENCIL:
                pencil.setPoint(pencil.getDraggedPoint().get(0), pencil.getDraggedPoint().get(0));
                paintState.addDrawState(pencil);
                pencil.draw(g2d);
                break;
            case PICKER:
                picker = new Picker();
                picker.setColor(new Color(buff_img.getRGB(start.x, start.y)));
                colorChooser.setColorPicker(picker.getColor());
                break;
            case CURVE:
                if(curve!=null){// thả chuột ra thì lưu điểm 
                    end = e.getPoint();
                    if(curve.getState()== 1){// trạng thái cuối cùng
                        curve.getList().get(3).setLocation(getPoint(e.getPoint()));// ghi trạng thái vào list 
                        curve.incState();// tăng bước lên 1
                        curve.addDraggedPoint(getPoint(end));//add vao keo chuot
                        curve.addPointToState(getPoint(end));// them vao mang replay
                        return;
                    }
                    else if(curve.getState() == 2){
                        curve.getList().get(1).setLocation(getPoint(e.getPoint()));
                        curve.getList().get(2).setLocation(getPoint(e.getPoint()));
                        curve.incState();
                        curve.addDraggedPoint(getPoint(end));
                        curve.addPointToState(getPoint(end));
                        return;
                    }
                    else if (curve.getState() == 3) {
                        paintState.addDrawState(curve);
                        curve.getList().get(2).setLocation(getPoint(e.getPoint()));
                        curve.draw(g2d);
                        curve.addDraggedPoint(getPoint(end));
                        curve.addPointToState(getPoint(end));   //Phan them buoc ve se cho xuong phia duoi
                        curve = null;
                        startCurve = false;
                    }
                }
                break;
            case SELECT:
                if(sel_rect != null){
                    //Neu anh dc chon thi ve len buffer
                    if(!sel_rect.isCreating()){// kiểm tra xem có đang chọn không
                        if(sel_rect.isDragging()){//Nếu đang kéo thì thả ra sẽ là điểm cuối
                            sel_rect.setEndOrigin(getPoint(end));
                            
                            //set lai la da tao dc anh
                            sel_rect.setIsCreating(true);
                            //Tao anh moi dua theo diem dau va diem cuoi
                            sel_rect.setImage(buff_img.getSubimage(Math.min(sel_rect.getStartOrigin().x, sel_rect.getEndOrigin().x),
                                    Math.min(sel_rect.getStartOrigin().y, sel_rect.getEndOrigin().y),
                                    Math.abs(sel_rect.getStartOrigin().x - sel_rect.getEndOrigin().x),
                                    Math.abs(sel_rect.getStartOrigin().y - sel_rect.getEndOrigin().y)));

                            sel_rect.setIsDragging(false);
                        }
                    }
                }  
                return;
                
            case TEXT:
                if (text != null) {
                    Font font = textPanel.getFont();
                    text.setEnd(e.getPoint());
                    text.setArea(this);
                    text.getArea().setFont(font);
                    text.getArea().setOpaque(textPanel.getIsOpaque());
                    text.setIsOpaque(textPanel.getIsOpaque());
                    text.getArea().setForeground(colorChooser.getStrokeColor());
                    text.setTextColor(colorChooser.getStrokeColor());
                    text.setFont(font);
                    text.setFillColor(colorChooser.getFillColor());
                    System.out.println("released: " + text.getEnd().x + ", " + text.getEnd().y);
                    repaint();
                    if (text.getIsCreated() == true) {
                        text.removeArea(this);
                        text = null;
                    }
                }
                return;
            }
            paintState.addDrawStep(PaintState.PAINTTING);
            start = null;
            end = null;
            repaint();
            
        
    }
    @Override
    public void mouseDragged(MouseEvent e) {
       //chỉ khi dragged chuột thì mới cập nhật hình lên bufer==>Lúc này mới ccaafn phải lưu ảnh
        isSaved = false;
        lbLocation.setText(getPoint(e.getPoint()).x + ", " + getPoint(e.getPoint()).y + "px");
        end = e.getPoint();
        switch (paintTool.getDrawMode()) {
            case LINE:
                line.setPoint(getPoint(start), getPoint(end));
                line.addDraggedPoint(getPoint(end));
                break;
            case RECTANGLE:
                rect.setPoint(getPoint(start), getPoint(end));
                rect.addDraggedPoint(getPoint(end));
                break;
            case TRIANGLE:
                triangle.setPoint(getPoint(start), getPoint(end));
                triangle.addDraggedPoint(getPoint(end));
                break;
            case OVAL:
                oval.setPoint(getPoint(start), getPoint(end));
                oval.addDraggedPoint(getPoint(end));
                break;
            case ERASER:
                eraser.setPoint(getPoint(start), getPoint(end));
                eraser.addDraggedPoint(getPoint(end));
                start = end;
                eraser.draw(g2d);
                locationEraser.move((int) (e.getPoint().x / zoom), (int) (e.getPoint().y / zoom));
                break;
            case PENCIL:
                pencil.setPoint(getPoint(start), getPoint(end));
                pencil.addDraggedPoint(getPoint(end));
                start = end;
                pencil.draw(g2d);
                break;
            case CURVE:
                if (curve != null) {
                    if (curve.getState() == 1) {
                        curve.getList().get(3).setLocation(getPoint(e.getPoint()));
                    } else if (curve.getState() == 2) {
                        curve.getList().get(2).setLocation(getPoint(e.getPoint()));
                    } else if (curve.getState() == 3) {
                        curve.getList().get(2).setLocation(getPoint(e.getPoint()));
                    }
                    curve.addDraggedPoint(getPoint(end));
                    curve.addPointToState(getPoint(end));

                }
                break;
            case SELECT:
                if (sel_rect != null) {
                    if (sel_rect.isCreating()) {
                        sel_rect.setStart(getPoint(new Point(end.x + temp1, end.y + temp2)));
                        sel_rect.addDraggedPoint(getPoint(new Point(end.x + temp1, end.y + temp2)));

                    } else {
                        sel_rect.setPoint(getPoint(new Point(Math.min(start.x,end.x),Math.min(start.y, end.y))),
                                getPoint(new Point(Math.max(start.x,end.x),Math.max(start.y,end.y))));
                        sel_rect.setIsDragging(true);
                    }
                }

                break;

            case TEXT:
                if (text != null) {
                    text.setEnd(getPoint(e.getPoint()));

                }
                repaint();
                return;
        }
        repaint(); 
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        lbLocation.setText(getPoint(e.getPoint()).x + ", " + getPoint(e.getPoint()).y + "px");

        if (paintTool.getDrawMode() == DrawMode.PICKER) {
            setCursor(cursorOfPicker);
        } else if (paintTool.getDrawMode() == DrawMode.ERASER) {
            isMouseExit = false;
            locationEraser.move((int) (e.getPoint().x / zoom), (int) (e.getPoint().y / zoom));
            repaint();
            setCursor(cursorOfEraser);
        } else if (paintTool.getDrawMode() == DrawMode.SELECT) {
            if (testHit(getPoint(e.getPoint())) == true) {
                setCursor(new Cursor(Cursor.MOVE_CURSOR));
            } else {
                setCursor(cursorOfPaint);
            }
        } else if (paintTool.getDrawMode() == DrawMode.BUCKET) {
            setCursor(cursorOfBucket);
          } else if (paintTool.getDrawMode() == DrawMode.PENCIL) {
            isMouseExit = false;
            
            repaint();
            setCursor(cursorOfPencil);
        } else {
            setCursor(cursorOfPaint);
        }
    }
}
