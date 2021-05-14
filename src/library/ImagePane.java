/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JProgressBar;
import library.Library;

/**
 *
 * @author HP
 */
public class ImagePane extends javax.swing.JPanel {
    
    private int rows = 3;
    private int cols = 5;
    private int currentRow;
    private int currentCol;
    private ImageCell[][] imageCells = new ImageCell[rows][cols];
    private ImageIcon[][] images = new ImageIcon[rows][cols];
    
    //Khung chua cac anh
    private BufferedImage buff_img;
    
    //Tao graphics cho khung panel: (la tap hop 15 anh show trong panel)
    private Graphics2D g2d;
    private Graphics2D g2;
    
    private final String org_path = "data/Library/";
    private String[] library = new String[7];
    
    //Chi so cua thu vien anh
    /*
    0 - animals
    1 - birthday cake
    2 - Cars
    3 - flowers
    4 - fruits
    5 - Hello kitty
    6 - houses
    */
    private int libIndex = -1;
    private BufferedImage selectedImage;
    private Point location;
    private ArrayList<ImageCell> storedImage;
    
    //Mang kiem tra anh da duoc load tu file ra chua
    private boolean[] isLoadeds = new boolean[7];
    private JProgressBar progressBar;
    /**
     * Creates new form ImagePane
     */
    public ImagePane() {
        initComponents();
        this.setSize(new Dimension(cols * ImageCell.WIDTH, rows * ImageCell.HEIGHT));
        
        library[0] = "animals/";
        library[1] = "birthday cake/";
        library[2] = "Cars/";
        library[3] = "flowers/";
        library[4] = "fruits/";
        library[5] = "Hello kitty/";
        library[6] = "houses/";
        
        for(int i = 0 ; i < 7 ; i++){
            isLoadeds[i] = false;
        }
        
        storedImage = new ArrayList<>();
        
        addMouseListener(new MouseAdapter(){
           public void mousePressed(MouseEvent e){
               location = e.getPoint();
               setImage();
               repaint();
           } 
        });
        
        addMouseMotionListener(new MouseAdapter(){
            public void mouseMoved(MouseEvent e){
                if(libIndex == -1){
                    return;
                }
                location = e.getPoint();
                repaint();
            }
        });
    }
    
    public void setProgressBar(JProgressBar progressBar){
        this.progressBar = progressBar;
    }
    
    //Phuong thuc load anh len panel
    public void refresh(){
        //Neu anh da duoc load
        g2 = (Graphics2D) buff_img.getGraphics();
        g2.setColor(new Color(204, 204, 255));
        g2.fillRect(0, 0, getSize().width, getSize().height);
        g2.dispose();
        
        if(isLoadeds[libIndex] == false){
            for(int i = 0 ; i < rows ; i++){
                for(int j = 0 ; j < cols ; j++){
                    /*
                    Dau tien, tempImg se doc anh tu file. 
                    Sau do phan tu images[i][j] se lay anh va scale lai size roi duoc in len ImageCells[i][j]
                    Sau do ImageCells[i][j] duoc ve len panel
                    */
                    
                    BufferedImage tempImg = null;
                    int k = i * cols + j;
                    
                    try{
                        tempImg = ImageIO.read(new File(org_path + library[libIndex] + k + ".png"));
                    }catch(IOException e){
                        System.out.println("Khong load duoc anh tu file/ Khong tim thay file");
                    }
                    
                    images[i][j] = new ImageIcon(tempImg.getScaledInstance(ImageCell.WIDTH, ImageCell.HEIGHT, Image.SCALE_SMOOTH));
                    imageCells[i][j] = new ImageCell(images[i][j]);
                    storedImage.add(imageCells[i][j]);
                    
                    //Ve image len panel
                    imageCells[i][j].paintIcon(null, g2d, j * ImageCell.WIDTH, i * ImageCell.HEIGHT);
                }
            }
            
            //Sau khi load anh xong => set gia tri isLoadeds cua trang = true
            isLoadeds[libIndex] = true;
        }
        // Neu da load tu file ra roi thi chi can lay anh tu storedImage de in ra panel
        else{
            ImageCell imageCell = null;
            for(int i = 0 ; i < rows ;  i++){
                for(int j = 0 ; j < cols ; j++){
                    int k = i * cols + j + cols * rows * libIndex;
                    imageCell = storedImage.get(k);
                    imageCell.paintIcon(null, g2d, j * ImageCell.WIDTH, i * ImageCell.HEIGHT);
                    
                    BufferedImage tempImg = (BufferedImage) imageCell.getImage();
                    images[i][j] = new ImageIcon(tempImg.getScaledInstance(ImageCell.WIDTH, ImageCell.HEIGHT, Image.SCALE_SMOOTH));
                }
            }
        }
    }
    
    //Chon selected image de co the day ra ngoai canvas
    public void setImage(){
        int k = currentRow * cols + currentCol;
        try{
            selectedImage = ImageIO.read(new File(org_path + library[libIndex] + k + ".png"));
        }catch(IOException e){
            System.out.println("Khong lay duoc anh");
        }
    }
    
    //Phuong thuc tra ve selectedImage
    public BufferedImage getSelectedImage(){
        return this.selectedImage;
    }
    
    public void drawToBuffer(int mouseX, int mouseY, Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        g2.setColor(new Color(204, 204, 255));
        g2.fillRect(0, 0, cols * ImageCell.WIDTH, rows * ImageCell.HEIGHT);
        
        currentRow = mouseY / ImageCell.HEIGHT;
        currentCol = mouseX / ImageCell.WIDTH;
        
        if(currentRow < 0 || currentRow > rows - 1 || currentCol < 0 || currentCol > cols - 1){
            return;
        } 
        
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2.drawImage(images[currentRow][currentCol].getImage(), currentCol * ImageCell.WIDTH, currentRow * ImageCell.HEIGHT, ImageCell.WIDTH, ImageCell.HEIGHT, null);
        //g2.setPaint(Color.BLUE);
    }
    
    //Chuyen sang trang tiep theo
    public void next(){
        if(libIndex < 6){
            //library index tang 1 
            libIndex++;
            //Tai lai anh len panel
            refresh();
            repaint();
        }
    }
    
    //Tro ve trang truoc
    public void prev(){
        if(libIndex > 0){
            libIndex--;
            refresh();
            repaint();
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(buff_img == null){
            buff_img = (BufferedImage) this.createImage(cols * ImageCell.WIDTH, rows * ImageCell.HEIGHT);
            g2d = (Graphics2D) buff_img.getGraphics();
            
            BufferedImage tempImg = null;
            try{
                //tempImg = ImageIO.read(Toolkit.getDefaultToolkit().getClass().getResource("library.png"));
                tempImg = ImageIO.read(new File(org_path + "library-01.png"));
            }catch(IOException e){
                System.out.println("Khong lay duoc anh");
            }
            
            tempImg.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
            g2d.drawImage(tempImg, 0, 0, this);
        }
        g.drawImage(buff_img, 0, 0, null);
        if(location != null){
            drawToBuffer(location.x, location.y, g);
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
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
