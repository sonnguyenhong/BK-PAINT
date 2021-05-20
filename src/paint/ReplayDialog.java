/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package paint;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author HP
 */
public class ReplayDialog extends javax.swing.JDialog {
    
    private PaintState paintState;
    private ImageIcon playIcon;
    private ImageIcon pauseIcon;
    private ImageIcon stopIcon;
    private ReplayPanel replayPanel;
    private JPanel containerPanel;
    private BufferedImage buff_img;
    
    private boolean isSaved = false;

    /** Creates new form ReplayDialog */
    public ReplayDialog(java.awt.Frame parent, boolean modal, PaintState paintState) {
        super(parent, modal);
        initComponents();
        
        playIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/data/icon/play.png")));
        pauseIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/data/icon/pause.png")));
        stopIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/data/icon/stop.png")));
        
        this.paintState = paintState;
        bPlay.setIcon(pauseIcon);
        bStop.setIcon(stopIcon);
        
        containerPanel = new JPanel();
        containerPanel.setLayout(null);
        
        replayPanel = new ReplayPanel();
        replayPanel.setPaintState(paintState);
        
        buff_img = replayPanel.getBuffer();
        
        containerPanel.setPreferredSize(new Dimension(replayPanel.getWidth() + 100, replayPanel.getHeight() + 50));
        containerPanel.add(replayPanel);
        containerPanel.validate();
        
        replayPanel.setButton(bPlay);
        this.addWindowListener(new WindowAdapter(){
            public void WindowClosing(WindowEvent e){
                if(isSaved == false){
                    Object[] option = {"Save", "Don't save", "Cancel"}; 
                    int specify = JOptionPane.showOptionDialog(null, "Do you want to save file?", "BKPaint", 
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, rootPane);
                    if(specify != JOptionPane.CANCEL_OPTION && specify != JOptionPane.CLOSED_OPTION){
                        if(specify == JOptionPane.YES_OPTION){
                            saveFile();
                        }
                        
                        if(isSaved == false){
                            return;
                        }
                    }else{
                        return;
                    }
                }
                replayPanel.flush();
                replayPanel.dispose();
            }
        });
        
        this.setTitle("Replay Dialog");
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    public void saveFile(){
        JFileChooser fileSave = new JFileChooser("Open replay file");
        int select = 0;
        File init = new File("Untitled.rep");
        fileSave.setSelectedFile(init);
        
        select = fileSave.showSaveDialog(null);
        
        if(select == JFileChooser.APPROVE_OPTION){
            File file = fileSave.getCurrentDirectory();
            String fileName = file.getPath() + "\\" + fileSave.getSelectedFile().getName();
            file = new File(fileName);
            if(file.exists()){
                int tmp = JOptionPane.showConfirmDialog(this, "File" + fileSave.getSelectedFile() + 
                        "already exists\nDo you want to replace it?", "Warning", 
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(tmp == JOptionPane.NO_OPTION){
                    return;
                }
            }
            try{
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
                oos.writeObject(paintState);
                isSaved = true;
                oos.close();
            }catch(IOException e){
                JOptionPane.showMessageDialog(null, "Save file error!", "Error", JOptionPane.ERROR_MESSAGE);
                //Logger.getLogger(ReplayDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void openFile(){
        JFileChooser fileOpen = new JFileChooser("Open replay file");
        int select = 0;
        replayPanel.flush();
        
        select = fileOpen.showOpenDialog(null);
        
        if(select == JFileChooser.APPROVE_OPTION){
            if(isSaved == false){
                Object[] option = {"Save", "Don't save", "Cancel"};
                int specify = JOptionPane.showOptionDialog(this, "Do you want to save this file?", "BKPaint",
                        JOptionPane.YES_NO_CANCEL_OPTION, 
                        JOptionPane.QUESTION_MESSAGE, null, option, rootPane);
                if(specify == JOptionPane.CANCEL_OPTION){
                    return;
                }
                if(specify == JOptionPane.YES_OPTION){
                    saveFile();
                }
            }
            try{
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileOpen.getSelectedFile()));
                String s = fileOpen.getSelectedFile().getName();
                this.setTitle(s + "- Replay Dialog");
                
                System.gc();
                
                paintState = (PaintState) ois.readObject();
                replayPanel.setPaintState(paintState);
                replayPanel.refresh();
                
                System.gc();
                ois.close();
            }catch(IOException e){
                System.out.println("Khong mo duoc file - error in openFile()");
            }catch(ClassNotFoundException e){
                System.out.println("Khong tim thay file - error in openFile()");
            }
        }
    }
    
    public void changeSpeed(int value){
        replayPanel.setDelay(value);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        bPlay = new javax.swing.JToggleButton();
        bStop = new javax.swing.JToggleButton();
        speed = new javax.swing.JSlider();
        jMenuBar1 = new javax.swing.JMenuBar();
        file = new javax.swing.JMenu();
        save = new javax.swing.JMenuItem();
        open = new javax.swing.JMenuItem();
        exit = new javax.swing.JMenuItem();
        help = new javax.swing.JMenu();
        info = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        buttonGroup1.add(bPlay);
        bPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPlayActionPerformed(evt);
            }
        });

        buttonGroup1.add(bStop);
        bStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bStopActionPerformed(evt);
            }
        });

        speed.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                speedStateChanged(evt);
            }
        });

        file.setText("File");

        save.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        save.setIcon(new javax.swing.ImageIcon("C:\\Users\\HP\\Documents\\NetBeansProjects\\BKPAINT\\data\\icon\\save.png")); // NOI18N
        save.setText("Save");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });
        file.add(save);

        open.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        open.setIcon(new javax.swing.ImageIcon("C:\\Users\\HP\\Documents\\NetBeansProjects\\BKPAINT\\data\\icon\\open.png")); // NOI18N
        open.setText("Open");
        open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openActionPerformed(evt);
            }
        });
        file.add(open);

        exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        exit.setIcon(new javax.swing.ImageIcon("C:\\Users\\HP\\Documents\\NetBeansProjects\\BKPAINT\\data\\icon\\exit.png")); // NOI18N
        exit.setText("Exit");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        file.add(exit);

        jMenuBar1.add(file);

        help.setText("Help");

        info.setIcon(new javax.swing.ImageIcon("C:\\Users\\HP\\Documents\\NetBeansProjects\\BKPAINT\\data\\icon\\information.png")); // NOI18N
        info.setText("About BKPaint");
        help.add(info);

        jMenuBar1.add(help);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(137, 137, 137)
                .addComponent(bPlay, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(bStop, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 247, Short.MAX_VALUE)
                .addComponent(speed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(286, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(speed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(bPlay)
                        .addComponent(bStop)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        // TODO add your handling code here:
        if(isSaved == false){
            saveFile();
        }
    }//GEN-LAST:event_saveActionPerformed

    private void openActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openActionPerformed
        // TODO add your handling code here:
        openFile();
    }//GEN-LAST:event_openActionPerformed

    private void speedStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_speedStateChanged
        // TODO add your handling code here:
        changeSpeed(speed.getValue());
    }//GEN-LAST:event_speedStateChanged

    private void bPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPlayActionPerformed
        // TODO add your handling code here:
        containerPanel.setSize(new Dimension(replayPanel.getWidth(), replayPanel.getHeight()));
        containerPanel.setMaximumSize(new Dimension(replayPanel.getWidth(), replayPanel.getHeight()));
        
        //Neu dang chay thi dung, neu dang dung thi chay
        if(replayPanel.isPlaying()){
            bPlay.setIcon(pauseIcon);
            replayPanel.pauseReplay();
        }else{
            bPlay.setIcon(playIcon);
            replayPanel.startReplay();
        }
    }//GEN-LAST:event_bPlayActionPerformed

    private void bStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bStopActionPerformed
        // TODO add your handling code here:
        bPlay.setIcon(pauseIcon);
        replayPanel.stopReplay();
    }//GEN-LAST:event_bStopActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        // TODO add your handling code here:
        if(isSaved == false){
            Object[] option = {"Save", "Don't save", "Cancel"};
            int specify = JOptionPane.showOptionDialog(null, "Do you want to save file?"
                    ,"BKPaint", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, rootPane);
            if(specify != JOptionPane.CANCEL_OPTION && specify != JOptionPane.CLOSED_OPTION){
                if(specify == JOptionPane.YES_OPTION){
                    saveFile();
                }
                if(isSaved == false){
                    return;
                }
            }else{
                return;
            }
        }
        this.setVisible(false);
        replayPanel.flush();
        replayPanel.dispose();
    }//GEN-LAST:event_exitActionPerformed
    
    /**
     * @param args the command line arguments
     
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
    /*
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ReplayDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReplayDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReplayDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReplayDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog 
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ReplayDialog dialog = new ReplayDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton bPlay;
    private javax.swing.JToggleButton bStop;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JMenuItem exit;
    private javax.swing.JMenu file;
    private javax.swing.JMenu help;
    private javax.swing.JMenuItem info;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem open;
    private javax.swing.JMenuItem save;
    private javax.swing.JSlider speed;
    // End of variables declaration//GEN-END:variables

}
